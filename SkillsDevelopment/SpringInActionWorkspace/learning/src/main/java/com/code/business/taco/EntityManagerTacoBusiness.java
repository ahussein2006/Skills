package com.code.business.taco;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.code.dal.entities.Taco;
import com.code.dal.repositories.base.EntityManagerDataAccess;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;

@Service
@Transactional
public class EntityManagerTacoBusiness {

	@Autowired
	private EntityManagerDataAccess entityManagerDataAccess;
	

	@Transactional(propagation = Propagation.NEVER)
	public void manageTaco(Taco design) throws BusinessException {
		try {
//			saveTaco(design);
			updateTaco(saveTaco(design));
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private Taco saveTaco(Taco design) throws BusinessException, RepositoryException {
		entityManagerDataAccess.addEntity(design, null);
		Taco t1 = new Taco();
		t1.setName("Test Taco 1");
		entityManagerDataAccess.addEntity(t1, null);
		
		t1.setName("Test Taco 2");
		Taco t2 = (Taco) entityManagerDataAccess.updateEntity(t1, null);
		
		Taco t10 = new Taco();
		t10.setId(10L);
		t10.setName("Test Taco 10 updated");
		t10.setVersion(0L);
		t10.setInsertedAt(new Date());
		Taco t11 = (Taco) entityManagerDataAccess.updateEntity(t10, null);
		
		entityManagerDataAccess.deleteEntity(t2, null);
		
		Taco t20 = new Taco();
		t20.setId(20L);
		t20.setVersion(0L);
		entityManagerDataAccess.deleteEntity(t20, null);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_NAME", "Test Taco 30");
		Taco t30 = entityManagerDataAccess.executeNamedQuery(Taco.class, "getTacoByName", params).get(0);
		t30.setName("Test Taco 30 updated");
		Taco t31 = (Taco) entityManagerDataAccess.updateEntity(t30, null);
		
		
		params = new HashMap<String, Object>();
		params.put("P_NAME", "Test Taco 40");
		Taco t40 = entityManagerDataAccess.executeNamedQuery(Taco.class, "getTacoByName", params).get(0);
		entityManagerDataAccess.deleteEntity(t40, null);
		
		
		System.out.println("done");
		return t31;
	}
	

	@Transactional(propagation = Propagation.REQUIRED)
	private Taco readTaco() throws BusinessException, RepositoryException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_NAME", "Test Taco 50");
		return entityManagerDataAccess.executeNamedQuery(Taco.class, "getTacoByName", params).get(0);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	private void updateTaco(Taco design) throws BusinessException, RepositoryException {
		design.setName("Test Taco 50 updated");
		Taco newDesign = (Taco) entityManagerDataAccess.updateEntity(design, null);
		System.out.println("done");
	}

}
