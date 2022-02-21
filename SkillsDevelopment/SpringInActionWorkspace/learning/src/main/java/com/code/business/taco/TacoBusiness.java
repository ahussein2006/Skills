package com.code.business.taco;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.code.dal.entities.Taco;
import com.code.dal.repositories.TacoRepository;
import com.code.exceptions.BusinessException;

@Service
@Transactional
public class TacoBusiness {

	@Autowired
	private TacoRepository tacoRepository;


	@Transactional(propagation = Propagation.SUPPORTS)
	public void manageTaco(Taco design, String userId) throws BusinessException {
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
			tacoRepository.save(design);

			// insert then update
			Taco t2 = new Taco();
			t2.setName("Test Taco 2");
			tacoRepository.save(t2);

			t2.setName("Test Taco 2 updated");
			Taco t200 = (Taco) tacoRepository.save(t2);

			// insert then update without calling update
			Taco t3 = new Taco();
			t3.setName("Test Taco 3");
			tacoRepository.save(t3);
			t3.setName("Test Taco 3 updated");

			// update detached
			Taco t10 = tacoRepository.getTacoUsingName("Test Taco 10");
			t10.setName("Test Taco 10 Updated");
			tacoRepository.save(t10);

			// update transient
			Taco t20 = new Taco();
			t20.setId(20L);
			t20.setVersion(0L);
			t20.setName("Test Taco 20 Updated");
			t20.setInsertedAt(new Date());
			tacoRepository.save(t20);

			// delete detached (doing another read but we accept it against the flush needed
			// when reading from the same session)
			Taco t30 = tacoRepository.getTacoUsingName("Test Taco 30");
			tacoRepository.delete(t30);

			// delete transient
			Taco t40 = new Taco();
			t40.setId(40L);
			t40.setVersion(0L);
			tacoRepository.delete(t40);

			// delete managed
			Taco t50 = new Taco();
			t50.setId(50L);
			t50.setVersion(0L);
			t50.setName("Test Taco 50 Updated");
			t50.setInsertedAt(new Date());
			tacoRepository.save(t50);
			tacoRepository.delete(t50);

			// multiple updates on the same object.
			Taco t70 = tacoRepository.getTacoUsingName("Test Taco 70");
			t70.setName("Test Taco 70 Updated");
			tacoRepository.save(t70);
			t70.setName("Test Taco 70 Updated Updated");
			tacoRepository.save(t70);

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
//			tacoRepository.save(t5);

//			// update non existing (successfully failed)
//			Taco t25 = new Taco();
//			t25.setId(25L);
//			t25.setVersion(0L);
//			t25.setName("Test Taco 25 Updated");
//			t25.setInsertedAt(new Date());
//			tacoRepository.save(t25);

			// delete non existing (not failed, make sure record exists before delete)
//			Taco t55 = new Taco();
//			t55.setId(55L);
//			t55.setVersion(0L);
//			t55.setName("Test Taco 55");
//			t55.setInsertedAt(new Date());
//			tacoRepository.delete(t55);


		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void saveAnotherTaco() throws BusinessException {
		try {
			Taco t2 = new Taco();
			t2.setName("Test Taco 5");
			tacoRepository.save(t2);
		} catch (Exception e) {
			throw new BusinessException("error_general");
		}
	}

}
