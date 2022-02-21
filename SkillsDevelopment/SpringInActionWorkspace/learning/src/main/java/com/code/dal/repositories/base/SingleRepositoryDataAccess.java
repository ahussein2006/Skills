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

import com.code.dal.entities.base.CommonEntity;
import com.code.exceptions.RepositoryException;

@Service
@Transactional
public class SingleRepositoryDataAccess{
	
	@Autowired
	private EntityManagerFactory entityManagerFactory;
	
	@Autowired
	private BaseRepository baseRepository;
	
    public  void addEntity(CommonEntity bean, String userId) {
    	baseRepository.save(bean);
    }
    
    public  CommonEntity updateEntity(CommonEntity bean, String userId)  {
    	return baseRepository.save(bean);
    }

    public  void deleteEntity(CommonEntity bean, String userId)  {
    	baseRepository.delete(bean);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
	public <T> List<T> executeNamedQuery(Class<T> dataClass, String queryName, Map<String, Object> parameters) throws RepositoryException {
    	EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Query q = em.createNamedQuery(queryName);

            if (parameters != null) {
                for (String paramName : parameters.keySet()) {
                    Object value = parameters.get(paramName);

                    q.setParameter(paramName, value);                    
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
