package com.code.dal.repositories.base;

import javax.persistence.EntityManager;

public class CustomEntityManager {
	private static final ThreadLocal<CustomEntityManager> entityManagersThreadLocal = new ThreadLocal<CustomEntityManager>();
	
	private final EntityManager entityManager;
	private final String entityManagerOwner;

	CustomEntityManager(EntityManager entityManager, String entityManagerOwner) {
		this.entityManager = entityManager;
		this.entityManagerOwner = entityManagerOwner;
	}
	
	static void setCurrentEntityManager(CustomEntityManager customEntityManager) {
		entityManagersThreadLocal.set(customEntityManager);
	}
	
	static CustomEntityManager getCurrentEntityManager() {
		return entityManagersThreadLocal.get();
	}
	
	static void removeCurrentEntityManager() {
		entityManagersThreadLocal.remove();
	}
	
	EntityManager getEntityManager() {
		return entityManager;
	}

	String getEntityManagerOwner() {
		return entityManagerOwner;
	}
	
}