package com.code.business.taco;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.WebApplicationContextLocator;
import com.code.exceptions.RepositoryException;



public class TestBusiness {

//	try to inject the bean factory 
	
	@Autowired
	private AMEntityManagerTacoBusiness tacoBusiness;
	
	public TestBusiness() {
        WebApplicationContextLocator.getCurrentWebApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
	}
	
	public void printTacoBusiness() {
		try {
			System.out.println("Test Business: " + tacoBusiness.readTaco("Test Taco 30").getName());
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
