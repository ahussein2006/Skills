package com.code.dal.repositories.base;

import org.springframework.data.repository.CrudRepository;

import com.code.dal.entities.base.CommonEntity;

public interface BaseRepository extends CrudRepository<CommonEntity, Long> {
	
}
