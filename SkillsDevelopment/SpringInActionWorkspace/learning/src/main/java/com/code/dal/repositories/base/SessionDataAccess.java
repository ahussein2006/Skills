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
import org.springframework.transaction.annotation.Transactional;

import com.code.dal.entities.base.BaseEntity;
import com.code.exceptions.RepositoryException;

@Service
@Transactional
public class SessionDataAccess {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private Session session;

	// --------------------- Entity Management -------------------

	public void addEntity(BaseEntity bean, String userId) {
		session.save(bean);
		// audit(bean, OperationsEnum.INSERT, userId, session);
	}

	public BaseEntity updateEntity(BaseEntity bean, String userId) {
		session.update(bean);
		// audit(bean, OperationsEnum.UPDATE, userId, session);
		return bean;
	}

	public void deleteEntity(BaseEntity bean, String userId) {
		session.delete(session.merge(bean));
		// audit(bean, OperationsEnum.DELETE, userId, session);
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
	@Transactional(readOnly = true)
	private <T> List<T> getEntities(Class<T> dataClass, String queryName, StringBuffer dynamicQueryBuffer,
			StringBuffer nativeQueryBuffer, Map<String, Object> parameters, Integer limit, Integer offset,
			boolean lockFlag) throws RepositoryException {

		Session querySession = lockFlag ? session : sessionFactory.openSession();

		try {
			Query<T> q = null;
			if (queryName != null)
				q = querySession.getNamedQuery(queryName);
			else if (dynamicQueryBuffer != null)
				q = querySession.createQuery(dynamicQueryBuffer.toString());
			else if (nativeQueryBuffer != null)
				q = querySession.createNativeQuery(nativeQueryBuffer.toString());

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
				querySession.close();
		}
	}

	// --------------------- Auditing ----------------------

	/*
	 * private static final Long moduleId = 1L; move up
	 * 
	 * private static void audit(BaseEntity bean, OperationsEnum operation, String
	 * userId, Session session) throws DatabaseException { if (bean
	 * instanceof AuditEntity) { AuditEntity auditableBean = (AuditEntity) bean;
	 * 
	 * // If the bean is not prevented from audit and there is no userId,
	 * we // throw database exception. if (!auditableBean.getPreventAuditFlag() &&
	 * (userId == null || userId.isEmpty())) { throw new
	 * DatabaseException("Transaction User cannot be null while auditing."); }
	 * 
	 * if (!auditableBean.getPreventAuditFlag()) { AuditLog log = new AuditLog();
	 * log.setModuleId(moduleId);
	 * 
	 * if (userId.matches("\\d+"))
	 * log.setUserId(Long.parseLong(userId)); else
	 * log.setSystemName(userId);
	 * 
	 * log.setOperation(operation.getCode()); log.setOperationGregDate(new Date());
	 * log.setContentEntity(auditableBean.getClass().getCanonicalName());
	 * log.setContentId(auditableBean.calculateContentId());
	 * log.setContent(auditableBean.calculateContent()); session.save(log); } } }
	 */

}