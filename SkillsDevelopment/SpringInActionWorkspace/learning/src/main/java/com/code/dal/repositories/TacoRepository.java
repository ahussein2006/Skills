package com.code.dal.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.code.dal.entities.Taco;

public interface TacoRepository extends CrudRepository<Taco, Long> {
	
	public Taco findByName(String name);
	
	@Query("select t from Taco t where t.name= :P_NAME")
	public Taco getTacoUsingName(@Param("P_NAME") String name);
	
}