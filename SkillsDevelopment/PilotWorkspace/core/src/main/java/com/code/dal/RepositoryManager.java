package com.code.dal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import com.code.dal.entities.base.AuditeeEntity;
import com.code.dal.entities.base.BaseEntity;
import com.code.dal.entities.um.audit.AuditLog;
import com.code.enums.OperationsEnum;
import com.code.enums.QueryConfigConstants;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;
import com.code.util.ConfigurationUtil;

@Service
public class RepositoryManager {

    private static SessionFactory sessionFactory;

    static {
	try {
	    StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
	    Metadata metadata = new MetadataSources(standardRegistry).getMetadataBuilder().build();
	    sessionFactory = metadata.getSessionFactoryBuilder().build();
	} catch (HibernateException exception) {
	    System.out.println("Problem creating session Factory!");
	    exception.printStackTrace();
	}
    }

    // ----------------------------------- PKG Session Retrieval -------------------------------

    Session getSession() {
	return sessionFactory.openSession();
    }

    // ----------------------------------- Transaction Management ------------------------------

    public void beginTransaction() throws RepositoryException {
	if (CustomSession.getCurrentSession() != null)
	    return;

	Session session = null;
	try {
	    session = sessionFactory.openSession();
	    session.beginTransaction();
	    CustomSession.setCurrentSession(new CustomSession(session, BasicUtil.getCallerMethod()));
	} catch (Exception e) {
	    if (session != null) {
		try {
		    session.getTransaction().rollback();
		} finally {
		    session.close();
		}
	    }
	    throw new RepositoryException(e.getMessage());
	}
    }

    public void flushTransaction() throws RepositoryException {
	try {
	    CustomSession.getCurrentSession().getSession().flush();
	} catch (Exception e) {
	    throw new RepositoryException(e.getMessage());
	}
    }

    public void commitTransaction() throws RepositoryException {
	CustomSession currentSession = CustomSession.getCurrentSession();
	if (!currentSession.getSessionOwner().equals(BasicUtil.getCallerMethod()))
	    return;

	try {
	    currentSession.getSession().getTransaction().commit();
	    currentSession.getSession().close();
	    CustomSession.removeCurrentSession();
	} catch (Exception e) {
	    throw new RepositoryException(e.getMessage());
	}
    }

    public void rollbackTransaction() {
	CustomSession currentSession = CustomSession.getCurrentSession();
	if (!currentSession.getSessionOwner().equals(BasicUtil.getCallerMethod()))
	    return;

	try {
	    currentSession.getSession().getTransaction().rollback();
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    currentSession.getSession().close();
	    CustomSession.removeCurrentSession();
	}
    }

    // ----------------------------------- Entity Management -----------------------------------

    public void insertEntity(BaseEntity entity, long userId) throws RepositoryException {
	try {
	    beginTransaction();

	    CustomSession.getCurrentSession().getSession().save(entity);
	    audit(entity, OperationsEnum.INSERT, userId);

	    commitTransaction();
	} catch (Exception e) {
	    rollbackTransaction();
	    throw new RepositoryException(e.getMessage());
	}
    }

    public BaseEntity updateEntity(BaseEntity entity, long userId) throws RepositoryException {
	try {
	    beginTransaction();

	    CustomSession.getCurrentSession().getSession().update(entity);
	    audit(entity, OperationsEnum.UPDATE, userId);

	    commitTransaction();
	} catch (Exception e) {
	    rollbackTransaction();
	    throw new RepositoryException(e.getMessage());
	}
	return entity;
    }

    public void deleteEntity(BaseEntity entity, long userId) throws RepositoryException {
	try {
	    beginTransaction();

	    CustomSession.getCurrentSession().getSession().delete(entity);
	    audit(entity, OperationsEnum.DELETE, userId);

	    commitTransaction();
	} catch (Exception e) {
	    rollbackTransaction();
	    throw new RepositoryException(e.getMessage());
	}
    }

    // ----------------------------------- Data Retrieval --------------------------------------

    public <T> List<T> getEntities(Class<T> dataClass, String queryName, String paramNames, Object... paramValues)
	    throws RepositoryException {
	return getEntities(dataClass, queryName, null, null, BasicUtil.getParamsMap(paramNames, paramValues), null, null, false);
    }

    public <T> List<T> getEntitiesWithPaging(Class<T> dataClass, String queryName,
	    int limit, int offset, String paramNames, Object... paramValues) throws RepositoryException {
	return getEntities(dataClass, queryName, null, null, BasicUtil.getParamsMap(paramNames, paramValues), limit, offset, false);
    }

    public <T> List<T> getEntitiesWithLocking(Class<T> dataClass, String queryName, String paramNames, Object... paramValues)
	    throws RepositoryException {
	return getEntities(dataClass, queryName, null, null, BasicUtil.getParamsMap(paramNames, paramValues), null, null, true);
    }

    @SuppressWarnings("unused")
    private <T> List<T> getEntitiesWithDynamicFiltering(Class<T> dataClass, StringBuffer dynamicQueryBuffer,
	    String paramNames, Object... paramValues) throws RepositoryException {
	return getEntities(dataClass, null, dynamicQueryBuffer, null, BasicUtil.getParamsMap(paramNames, paramValues), null, null, false);
    }

    @SuppressWarnings("unused")
    private <T> List<T> getEntitiesWithNativeAccessing(Class<T> dataClass, StringBuffer nativeQueryBuffer,
	    String paramNames, Object... paramValues) throws RepositoryException {
	return getEntities(dataClass, null, null, nativeQueryBuffer, BasicUtil.getParamsMap(paramNames, paramValues), null, null, false);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getEntities(Class<T> dataClass, String queryName, StringBuffer dynamicQueryBuffer,
	    StringBuffer nativeQueryBuffer, Map<String, Object> parameters, Integer limit, Integer offset,
	    boolean lockFlag) throws RepositoryException {

	Session session = lockFlag ? CustomSession.getCurrentSession().getSession() : sessionFactory.openSession();

	try {
	    Query<T> q = null;
	    if (queryName != null)
		q = session.getNamedQuery(queryName);
	    else if (dynamicQueryBuffer != null)
		q = session.createQuery(dynamicQueryBuffer.toString());
	    else if (nativeQueryBuffer != null)
		q = session.createNativeQuery(nativeQueryBuffer.toString());

	    setQueryParameters(q, parameters);

	    if (limit != null && offset != null) {
		q.setFirstResult(offset);
		q.setMaxResults(limit);
	    }

	    if (lockFlag)
		q.setLockMode(LockModeType.PESSIMISTIC_WRITE);

	    List<T> result = q.getResultList();
	    return BasicUtil.isNullOrEmpty(result) ? new ArrayList<T>() : result;
	} catch (Exception e) {
	    throw new RepositoryException(e.getMessage());
	} finally {
	    if (!lockFlag)
		session.close();
	}
    }

    private <T> void setQueryParameters(Query<T> q, Map<String, Object> parameters) {
	for (String parameterName : q.getParameterMetadata().getNamedParameterNames()) {
	    switch (parameterName) {
	    case QueryConfigConstants.P_ESC_SEARCH_STR:
		setQueryParameter(q, parameterName, BasicUtil.getEscapeString());
		break;
	    case QueryConfigConstants.P_ESC_SEARCH_LONG:
		setQueryParameter(q, parameterName, BasicUtil.getEscapeLong());
		break;
	    case QueryConfigConstants.P_ESC_SEARCH_INT:
		setQueryParameter(q, parameterName, BasicUtil.getEscapeInteger());
		break;
	    case QueryConfigConstants.P_ESC_SEARCH_DOUBLE:
		setQueryParameter(q, parameterName, BasicUtil.getEscapeDouble());
		break;
	    case QueryConfigConstants.P_MODULE_ID:
		setQueryParameter(q, parameterName, ConfigurationUtil.getModuleId());
		break;
	    default:
		setQueryParameter(q, parameterName, parameters.get(parameterName));
	    }
	}
    }

    private <T> void setQueryParameter(Query<T> q, String parameterName, Object parameterValue) {
	if (parameterValue instanceof Integer)
	    q.setParameter(parameterName, parameterValue, org.hibernate.type.IntegerType.INSTANCE);
	else if (parameterValue instanceof String)
	    q.setParameter(parameterName, parameterValue, org.hibernate.type.StringType.INSTANCE);
	else if (parameterValue instanceof Long)
	    q.setParameter(parameterName, parameterValue, org.hibernate.type.LongType.INSTANCE);
	else if (parameterValue instanceof Float)
	    q.setParameter(parameterName, parameterValue, org.hibernate.type.FloatType.INSTANCE);
	else if (parameterValue instanceof Double)
	    q.setParameter(parameterName, parameterValue, org.hibernate.type.DoubleType.INSTANCE);
	else if (parameterValue instanceof Date)
	    q.setParameter(parameterName, parameterValue, org.hibernate.type.DateType.INSTANCE);
	else if (parameterValue instanceof Integer[])
	    q.setParameterList(parameterName, (Integer[]) parameterValue, org.hibernate.type.IntegerType.INSTANCE);
	else if (parameterValue instanceof String[])
	    q.setParameterList(parameterName, (String[]) parameterValue, org.hibernate.type.StringType.INSTANCE);
	else if (parameterValue instanceof Long[])
	    q.setParameterList(parameterName, (Long[]) parameterValue, org.hibernate.type.LongType.INSTANCE);
    }

    // ----------------------------------- Auditing --------------------------------------------
    private void audit(BaseEntity entity, OperationsEnum operation, long userId) throws RepositoryException {
	if (entity instanceof AuditeeEntity) {
	    AuditeeEntity auditableEntity = (AuditeeEntity) entity;

	    if (!auditableEntity.isPreventAuditFlag()) {
		AuditLog log = new AuditLog();
		log.setModuleId(ConfigurationUtil.getModuleId());
		log.setUserId(userId);
		log.setOperation(operation.toString());
		log.setOperationDate(new Date());
		log.setContentEntity(auditableEntity.getClass().getCanonicalName());
		log.setContentId(auditableEntity.caculateContentId());
		log.setContent(auditableEntity.calculateContent());
		CustomSession.getCurrentSession().getSession().save(log);
	    }
	}
    }
}
