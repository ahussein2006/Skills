package com.code.business.taco;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.QueryNames;
import com.code.dal.entities.Taco;
import com.code.dal.repositories.base.AMSessionDataAccess;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;

@Service
public class AMSessionTacoBusiness {
	
	@Autowired
	private AMSessionDataAccess sessionDataAccess;
	
	

	public Taco addOrUpdateTaco(Taco design, String userId) throws BusinessException {
		try {
			sessionDataAccess.beginTransaction();

			if (design.getId() == null) {
				sessionDataAccess.addEntity(design, userId);
			} else {
				sessionDataAccess.updateEntity(design, userId);
			}

			sessionDataAccess.commitTransaction();
		} catch (Exception e) {
			sessionDataAccess.rollbackTransaction();
			throw new BusinessException("error_general");
		}
		return design;
	}

	public void manageTaco(Taco design, String userId) throws BusinessException {
		saveTaco(design, userId);
//		saveAnotherTaco();
	}

	private void saveTaco(Taco design, String userId) throws BusinessException {
		try {

			sessionDataAccess.beginTransaction();

			// insert new
			sessionDataAccess.addEntity(design, userId);

			// insert then update
			Taco t2 = new Taco();
			t2.setName("Test Taco 2");
			sessionDataAccess.addEntity(t2, userId);

			t2.setName("Test Taco 2 updated");
			sessionDataAccess.updateEntity(t2, userId);

			// insert then update without calling update
			Taco t3 = new Taco();
			t3.setName("Test Taco 3");
			sessionDataAccess.addEntity(t3, userId);
			t3.setName("Test Taco 3 updated");

			// update detached
			Taco t10 = getTacoByName("Test Taco 10");
			t10.setName("Test Taco 10 Updated");
			sessionDataAccess.updateEntity(t10, userId);

			// update transient
			Taco t20 = new Taco();
			t20.setId(20L);
			t20.setVersion(0L);
			t20.setName("Test Taco 20 Updated");
			t20.setInsertedAt(new Date());
			sessionDataAccess.updateEntity(t20, userId);

			// delete detached (doing another read but we accept it against the flush needed
			// when reading from the same session)
			Taco t30 = getTacoByName("Test Taco 30");
			sessionDataAccess.deleteEntity(t30, userId);

			// delete transient
			Taco t40 = new Taco();
			t40.setId(40L);
			t40.setVersion(0L);
			sessionDataAccess.deleteEntity(t40, userId);

			// delete managed
			Taco t50 = new Taco();
			t50.setId(50L);
			t50.setVersion(0L);
			t50.setName("Test Taco 50 Updated");
			t50.setInsertedAt(new Date());
			sessionDataAccess.updateEntity(t50, userId);
			sessionDataAccess.deleteEntity(t50, userId);

			// multiple updates on the same object.
			Taco t70 = getTacoByName("Test Taco 70");
			t70.setName("Test Taco 70 Updated");
			sessionDataAccess.updateEntity(t70, userId);
			t70.setName("Test Taco 70 Updated Updated");
			sessionDataAccess.updateEntity(t70, userId);

			saveAdditionalTaco();

			sessionDataAccess.commitTransaction();
		} catch (Exception e) {
			sessionDataAccess.rollbackTransaction();
			throw new BusinessException("error_general");
		}
	}

	private void saveAdditionalTaco() throws BusinessException {
		try {

			sessionDataAccess.beginTransaction();

			// insert existing (successfully failed)
//			Taco t5 = new Taco();
//			t5.setName("Test Taco 5");
//			sessionDataAccess.addEntity(t5, null);

			// update non existing (successfully failed)
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

			sessionDataAccess.commitTransaction();
		} catch (Exception e) {
			sessionDataAccess.rollbackTransaction();
			throw new BusinessException("error_general");
		}
	}

	private void saveAnotherTaco() throws BusinessException {
		try {

			sessionDataAccess.beginTransaction();

			Taco t2 = new Taco();
			t2.setName("Test Taco 5");
			sessionDataAccess.addEntity(t2, null);

			sessionDataAccess.commitTransaction();
		} catch (Exception e) {
			sessionDataAccess.rollbackTransaction();
			throw new BusinessException("error_general");
		}
	}

	private Taco getTacoByName(String tacoName) throws RepositoryException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_NAME", tacoName);
		return sessionDataAccess.getEntities(Taco.class, QueryNames.TACO_GET_TACO_BY_NAME, params).get(0);
	}

	public Taco getTacoById(Long id) throws BusinessException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("P_ID", id);
			return sessionDataAccess.getEntities(Taco.class, QueryNames.TACO_GET_TACO_BY_ID, params).get(0);
		} catch (RepositoryException e) {
			throw new BusinessException("error_general");
		}
	}

	private Taco getTacoByNameWithLock(String tacoName) throws RepositoryException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("P_NAME", tacoName);
		return sessionDataAccess.getEntitiesWithLocking(Taco.class, QueryNames.TACO_GET_TACO_BY_NAME, params).get(0);
	}

	public List<Taco> getRecentTacos() throws BusinessException {
		try {
//			TestBusiness testBusiness = new TestBusiness();
//			testBusiness.printTacoBusiness();
			
			return sessionDataAccess.getEntitiesWithPaging(Taco.class, QueryNames.TACO_GET_RECENT_TACOS, null, 5, 0);
		} catch (RepositoryException e) {
			throw new BusinessException("error_general");
		}
	}

}
