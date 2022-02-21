package com.code.business.taco;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.code.dal.entities.Taco;
import com.code.dal.entities.base.BaseEntity;
import com.code.dal.repositories.base.AMEntityManagerDataAccess;
import com.code.dal.repositories.base.SessionDataAccess;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;

@Service
@Transactional
public class SessionTacoBusiness {

	@Autowired
	private SessionDataAccess sessionDataAccess;

	@Transactional(propagation = Propagation.SUPPORTS)
	public void manageTaco(Taco design) throws BusinessException {
		try {
			saveTaco(design);
//			saveAnotherTaco();
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void saveTaco(Taco design) throws BusinessException {
		try {

			// insert new
			sessionDataAccess.addEntity(design, null);

			// insert then update
			Taco t2 = new Taco();
			t2.setName("Test Taco 2");
			sessionDataAccess.addEntity(t2, null);

			t2.setName("Test Taco 2 updated");
			Taco t200 = (Taco) sessionDataAccess.updateEntity(t2, null);

			// insert then update without calling update
			Taco t3 = new Taco();
			t3.setName("Test Taco 3");
			sessionDataAccess.addEntity(t3, null);
			t3.setName("Test Taco 3 updated");

			// update detached
			Taco t10 = readTaco("Test Taco 10");
			t10.setName("Test Taco 10 Updated");
			sessionDataAccess.updateEntity(t10, null);

			// update transient
			Taco t20 = new Taco();
			t20.setId(20L);
			t20.setVersion(0L);
			t20.setName("Test Taco 20 Updated");
			t20.setInsertedAt(new Date());
			sessionDataAccess.updateEntity(t20, null);

			// delete detached (doing another read but we accept it against the flush needed
			// when reading from the same session)
			Taco t30 = readTaco("Test Taco 30");
			sessionDataAccess.deleteEntity(t30, null);

			// delete transient
			Taco t40 = new Taco();
			t40.setId(40L);
			t40.setVersion(0L);
			sessionDataAccess.deleteEntity(t40, null);

			// delete managed
			Taco t50 = new Taco();
			t50.setId(50L);
			t50.setVersion(0L);
			t50.setName("Test Taco 50 Updated");
			t50.setInsertedAt(new Date());
			sessionDataAccess.updateEntity(t50, null);
			sessionDataAccess.deleteEntity(t50, null);

			// multiple updates on the same object.
			Taco t70 = readTaco("Test Taco 70");
			t70.setName("Test Taco 70 Updated");
			sessionDataAccess.updateEntity(t70, null);
			t70.setName("Test Taco 70 Updated Updated");
			sessionDataAccess.updateEntity(t70, null);

			saveAdditionalTaco();

		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void saveAdditionalTaco() throws BusinessException {
		try {


			// insert existing (successfully failed)
//			Taco t5 = new Taco();
//			t5.setName("Test Taco 5");
//			sessionDataAccess.addEntity(t5, null);

//			// update non existing (successfully failed)
//			Taco t25 = new Taco();
//			t25.setId(25L);
//			t25.setVersion(0L);
//			t25.setName("Test Taco 25 Updated");
//			t25.setInsertedAt(new Date());
//			sessionDataAccess.updateEntity(t25, null);

			// delete non existing (not failed, make sure record exists before delete)
//			Taco t55 = new Taco();
//			t55.setId(55L);
//			t55.setVersion(0L);
//			t55.setName("Test Taco 55");
//			t55.setInsertedAt(new Date());
//			sessionDataAccess.deleteEntity(t55, null);

			// edit in an object without calling save or update with the same session.
			// (managed entities don't need update call and it is not recommended behavior
			// and that's why we don't use the same session in reading)
//			Taco t60 = readTacoWithLock("Test Taco 60");
//			t60.setName("Test Taco 60 Updated");

		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void saveAnotherTaco() throws BusinessException {
		try {
			Taco t2 = new Taco();
			t2.setName("Test Taco 5");
			sessionDataAccess.addEntity(t2, null);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	private Taco readTaco(String tacoName) throws RepositoryException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_NAME", tacoName);
		return sessionDataAccess.getEntities(Taco.class, "getTacoByName", params).get(0);
	}

	private Taco readTacoWithLock(String tacoName) throws RepositoryException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_NAME", tacoName);
		return sessionDataAccess.getEntitiesWithLocking(Taco.class, "getTacoByName", params).get(0);
	}

}
