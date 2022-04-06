package com.code;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.RepositoryManager;
import com.code.dal.entities.QueryConfiguration;
import com.code.dal.entities.um.audit.AuditLog;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;

@Service
public class Test {

    @Autowired
    private RepositoryManager repositoryManager;

    public void addAuditLog(AuditLog auditLog) {
	try {
	    repositoryManager.insertEntity(auditLog, 1);
	} catch (RepositoryException e) {
	    e.printStackTrace();
	}
    }

    public AuditLog getMessage(String contentId) {
	return BasicUtil.getFirstItem(searchAuditLogs(contentId));
    }

    private List<AuditLog> searchAuditLogs(String contentId) {
	try {
	    return repositoryManager.getEntities(AuditLog.class, QueryConfiguration.UM_AUDIT_LOG_GET_AUDIT_LOGS, QueryConfiguration.UM_AUDIT_LOG_GET_AUDIT_LOGS_PARAMS,
		    BasicUtil.getValueOrEscape(""), contentId, BasicUtil.getValueOrEscape(""), BasicUtil.getValueOrEscape((Long) null),
		    BasicUtil.getValueOrEscape((Integer) null), "06/04/2022",
		    BasicUtil.getValueOrEscape((Integer) null), "06/04/2022",
		    BasicUtil.getValueLikeOrEscape(""),
		    BasicUtil.getEscapeString(), BasicUtil.getEscapeLong(), BasicUtil.getEscapeInteger());
	} catch (RepositoryException e) {
	    return new ArrayList<AuditLog>();
	}
    }
}
