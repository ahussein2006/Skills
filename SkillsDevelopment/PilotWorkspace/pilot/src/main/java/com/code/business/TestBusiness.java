package com.code.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.Test;
import com.code.dal.entities.um.audit.AuditLog;

@Service
public class TestBusiness {

    @Autowired
    Test test;

    public void addAuditLog(AuditLog auditLog) {
	test.addAuditLog(auditLog);
    }

    public AuditLog getAuditLogById(String contentId) {
	return test.getMessage(contentId);
    }
}
