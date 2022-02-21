package com.code.business.taco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.code.dal.entities.Taco;
import com.code.dal.repositories.base.SingleRepositoryDataAccess;
import com.code.exceptions.BusinessException;

@Service
@Transactional
public class SingleRepositoryTacoBusiness {

	@Autowired
	private SingleRepositoryDataAccess singleRepositoryDataAccess;


	@Transactional(propagation = Propagation.SUPPORTS)
	public void manageTaco(Taco design) throws BusinessException {
		try {
			saveTaco(design);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void saveTaco(Taco design) throws BusinessException {
		singleRepositoryDataAccess.addEntity(design, null);
		Taco t1 = new Taco();
		t1.setName("Test Taco 1");
		singleRepositoryDataAccess.addEntity(t1, null);
		
		Taco t2 = new Taco();
		t2.setName("Test Taco 1");
		singleRepositoryDataAccess.addEntity(t2, null);
	}

}
