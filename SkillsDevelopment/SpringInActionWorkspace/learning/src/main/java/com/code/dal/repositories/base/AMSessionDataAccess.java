package com.code.dal.repositories.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.entities.audit.AuditeeEntity;
import com.code.dal.entities.audit.AuditLog;
import com.code.dal.entities.base.BaseEntity;
import com.code.enums.OperationsEnum;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;

@Service
public class AMSessionDataAccess {

	private static final Long moduleId = 1L;
	
	@Autowired
	private SessionFactory sessionFactory;

	// --------------------- Transaction Management --------------

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

	// --------------------- Entity Management -------------------

	public void insertEntity(BaseEntity entity, String userId) throws RepositoryException {
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

	public BaseEntity updateEntity(BaseEntity entity, String userId) throws RepositoryException {
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

	public void deleteEntity(BaseEntity entity, String userId) throws RepositoryException {
		try {
			beginTransaction();

			CustomSession.getCurrentSession().getSession()
					.delete(CustomSession.getCurrentSession().getSession().merge(entity));
			audit(entity, OperationsEnum.DELETE, userId);

			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw new RepositoryException(e.getMessage());
		}
	}

	// --------------------- Data Retrieval ----------------------

	public <T> List<T> getEntities(Class<T> dataClass, String queryName, Map<String, Object> parameters)
			throws RepositoryException {
		return getEntities(dataClass, queryName, null, null, parameters, null, null, false);
	}

	public <T> List<T> getEntitiesWithPaging(Class<T> dataClass, String queryName, Map<String, Object> parameters,
			int limit, int offset) throws RepositoryException {
		return getEntities(dataClass, queryName, null, null, parameters, limit, offset, false);
	}

	public <T> List<T> getEntitiesWithLocking(Class<T> dataClass, String queryName, Map<String, Object> parameters)
			throws RepositoryException {
		return getEntities(dataClass, queryName, null, null, parameters, null, null, true);
	}

	@SuppressWarnings("unused")
	private <T> List<T> getEntitiesWithDynamicFiltering(Class<T> dataClass, StringBuffer dynamicQueryBuffer,
			Map<String, Object> parameters) throws RepositoryException {
		return getEntities(dataClass, null, dynamicQueryBuffer, null, parameters, null, null, false);
	}

	@SuppressWarnings("unused")
	private <T> List<T> getEntitiesWithNativeAccessing(Class<T> dataClass, StringBuffer nativeQueryBuffer,
			Map<String, Object> parameters) throws RepositoryException {
		return getEntities(dataClass, null, null, nativeQueryBuffer, parameters, null, null, false);
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

			if (parameters != null) {
				for (String paramName : parameters.keySet()) {
					Object value = parameters.get(paramName);

					if (value instanceof Integer)
						q.setParameter(paramName, value, org.hibernate.type.IntegerType.INSTANCE);
					else if (value instanceof String)
						q.setParameter(paramName, value, org.hibernate.type.StringType.INSTANCE);
					else if (value instanceof Long)
						q.setParameter(paramName, value, org.hibernate.type.LongType.INSTANCE);
					else if (value instanceof Float)
						q.setParameter(paramName, value, org.hibernate.type.FloatType.INSTANCE);
					else if (value instanceof Double)
						q.setParameter(paramName, value, org.hibernate.type.DoubleType.INSTANCE);
					else if (value instanceof Date)
						q.setParameter(paramName, value, org.hibernate.type.DateType.INSTANCE);
					else if (value instanceof Integer[])
						q.setParameterList(paramName, (Integer[]) value, org.hibernate.type.IntegerType.INSTANCE);
					else if (value instanceof String[])
						q.setParameterList(paramName, (String[]) value, org.hibernate.type.StringType.INSTANCE);
					else if (value instanceof Long[])
						q.setParameterList(paramName, (Long[]) value, org.hibernate.type.LongType.INSTANCE);
				}
			}

			if (limit != null && offset != null) {
				q.setFirstResult(offset);
				q.setMaxResults(limit);
			}

			if (lockFlag)
				q.setLockMode(LockModeType.PESSIMISTIC_WRITE);

			List<T> result = q.getResultList();

			if (result == null || result.size() == 0)
				result = new ArrayList<T>();

			return result;
		} catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		} finally {
			if (!lockFlag)
				session.close();
		}
	}

	// --------------------- Auditing ------------------------
	private void audit(BaseEntity entity, OperationsEnum operation, String userId) throws RepositoryException {
		if (entity instanceof AuditeeEntity) {
			AuditeeEntity auditableEntity = (AuditeeEntity) entity;

			if (!auditableEntity.isPreventAuditFlag()) {

				if (userId == null || userId.isEmpty())
					throw new RepositoryException("User cannot be null while auditing.");

				AuditLog log = new AuditLog();
				log.setModuleId(moduleId);

				if (userId.matches("\\d+"))
					log.setUserId(Long.parseLong(userId));
				else
					log.setSystemName(userId);

				log.setOperation(operation.toString());
				log.setOperationGregDate(new Date());
				log.setContentEntity(auditableEntity.getClass().getCanonicalName());
				log.setContentId(auditableEntity.getId());
				log.setContent(auditableEntity.calculateContent());
				insertEntity(log, null);
			}
		}
	}
}