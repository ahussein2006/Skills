package com.code.dal.repositories.base;

import org.springframework.data.repository.CrudRepository;

import com.code.dal.entities.audit.AuditLog;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {
	
}