package com.code.dal.repositories.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.code.dal.entities.base.BaseEntity;
import com.code.exceptions.RepositoryException;

@Service
@Transactional
public class EntityManagerDataAccess {

	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private EntityManager entityManager;

	public void addEntity(BaseEntity bean, String userId) {
		entityManager.persist(bean);
	}

	public BaseEntity updateEntity(BaseEntity bean, String userId) {
		return entityManager.merge(bean);
	}

	public void deleteEntity(BaseEntity bean, String userId) {
		entityManager.remove(entityManager.merge(bean));
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public <T> List<T> executeNamedQuery(Class<T> dataClass, String queryName, Map<String, Object> parameters)
			throws RepositoryException {
		EntityManager em = entityManagerFactory.createEntityManager();
		
		try {
			Query q = em.createNamedQuery(queryName);

			if (parameters != null) {
				for (String paramName : parameters.keySet()) {
					q.setParameter(paramName, parameters.get(paramName));
				}
			}

			List<T> result = q.getResultList();

			if (result == null || result.size() == 0)
				result = new ArrayList<T>();

			return result;

		} catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		}
		finally{
        	em.close();
		}
	}

}
