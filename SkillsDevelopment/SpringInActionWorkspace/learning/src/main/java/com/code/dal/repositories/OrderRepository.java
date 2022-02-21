package com.code.dal.repositories;

import org.springframework.data.repository.CrudRepository;

import com.code.dal.entities.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
	
}