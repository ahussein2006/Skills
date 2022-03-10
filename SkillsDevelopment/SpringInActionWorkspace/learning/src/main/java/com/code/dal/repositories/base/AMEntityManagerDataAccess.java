package com.code.dal.repositories.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.entities.base.BaseEntity;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;

@Service
public class AMEntityManagerDataAccess {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	// --------------------- Transaction Management --------------

	public void beginTransaction() throws RepositoryException {
		if (CustomEntityManager.getCurrentEntityManager() != null)
			return;

		EntityManager entityManager = null;
		try {
			entityManager = entityManagerFactory.createEntityManager();
			entityManager.getTransaction().begin();
			CustomEntityManager.setCurrentEntityManager(new CustomEntityManager(entityManager, BasicUtil.getCallerMethod()));
		} catch (Exception e) {
			if (entityManager != null) {
				try {
					entityManager.getTransaction().rollback();
				} finally {
					entityManager.close();
				}
			}
			throw new RepositoryException(e.getMessage());
		}
	}

	public void flushTransaction() throws RepositoryException {
		try {
			CustomEntityManager.getCurrentEntityManager().getEntityManager().flush();
		} catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	public void commitTransaction() throws RepositoryException {
		CustomEntityManager currentEntityManager = CustomEntityManager.getCurrentEntityManager();
		if (!currentEntityManager.getEntityManagerOwner().equals(BasicUtil.getCallerMethod()))
			return;

		try {
			currentEntityManager.getEntityManager().getTransaction().commit();
			currentEntityManager.getEntityManager().close();
			CustomEntityManager.removeCurrentEntityManager();
		} catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		}
	}

	public void rollbackTransaction() {
		CustomEntityManager currentEntityManager = CustomEntityManager.getCurrentEntityManager();
		if (!currentEntityManager.getEntityManagerOwner().equals(BasicUtil.getCallerMethod()))
			return;

		try {
			currentEntityManager.getEntityManager().getTransaction().rollback();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			currentEntityManager.getEntityManager().close();
			CustomEntityManager.removeCurrentEntityManager();
		}
	}

	// --------------------- Entity Management -------------------

	public void insertEntity(BaseEntity entity, String userId) throws RepositoryException {
		try {
			beginTransaction();

			CustomEntityManager.getCurrentEntityManager().getEntityManager().persist(entity);
			// audit(entity, OperationsEnum.INSERT, userId, entityManager);

			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw new RepositoryException(e.getMessage());
		}
	}

	public BaseEntity updateEntity(BaseEntity entity, String userId) throws RepositoryException {
		BaseEntity updatedEntity = null;
		
		try {
			beginTransaction();

			updatedEntity = CustomEntityManager.getCurrentEntityManager().getEntityManager().merge(entity);
			// audit(entity, OperationsEnum.UPDATE, userId, entityManager);

			commitTransaction();
		} catch (Exception e) {
			rollbackTransaction();
			throw new RepositoryException(e.getMessage());
		}
		
		return updatedEntity;
	}

	public void deleteEntity(BaseEntity entity, String userId) throws RepositoryException {
		try {
			beginTransaction();

			CustomEntityManager.getCurrentEntityManager().getEntityManager().remove(CustomEntityManager.getCurrentEntityManager().getEntityManager().merge(entity));
			// audit(entity, OperationsEnum.DELETE, userId, entityManager);

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

		EntityManager entityManager = lockFlag ? CustomEntityManager.getCurrentEntityManager().getEntityManager() : entityManagerFactory.createEntityManager();

		try {
			Query q = null;
			if (queryName != null)
				q = entityManager.createNamedQuery(queryName);
			else if (dynamicQueryBuffer != null)
				q = entityManager.createQuery(dynamicQueryBuffer.toString());
			else if (nativeQueryBuffer != null)
				q = entityManager.createNativeQuery(nativeQueryBuffer.toString());

			if (parameters != null) {
				for (String paramName : parameters.keySet()) {
					q.setParameter(paramName, parameters.get(paramName));
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
				entityManager.close();
		}
	}

	// --------------------- Auditing ----------------------

	/*
	 * private static final Long moduleId = 1L; move up
	 * 
	 * private static void audit(BaseEntity entity, OperationsEnum operation, String
	 * userId, EntityManager entityManager) throws DatabaseException { if (entity
	 * instanceof AuditEntity) { AuditEntity auditableEntity = (AuditEntity) bean;
	 * 
	 * // If the bean is not prevented from audit and there is no userId,
	 * we // throw database exception. if (!auditableEntity.getPreventAuditFlag() &&
	 * (userId == null || userId.isEmpty())) { throw new
	 * DatabaseException("Transaction User cannot be null while auditing."); }
	 * 
	 * if (!auditableEntity.getPreventAuditFlag()) { AuditLog log = new AuditLog();
	 * log.setModuleId(moduleId);
	 * 
	 * if (userId.matches("\\d+"))
	 * log.setUserId(Long.parseLong(userId)); else
	 * log.setSystemName(userId);
	 * 
	 * log.setOperation(operation.getCode()); log.setOperationGregDate(new Date());
	 * log.setContentEntity(auditableEntity.getClass().getCanonicalName());
	 * log.setContentId(auditableEntity.calculateContentId());
	 * log.setContent(auditableEntity.calculateContent()); entityManager.save(log); } } }
	 */

}