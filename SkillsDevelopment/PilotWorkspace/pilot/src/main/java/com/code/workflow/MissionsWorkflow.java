package com.code.workflow;

import org.springframework.stereotype.Service;

import com.code.dal.entities.workflow.WFInstance;
import com.code.dal.entities.workflow.missions.WFMission;
import com.code.enums.ErrorMessageCodesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;
import com.code.util.ExceptionUtil;

@Service
public class MissionsWorkflow extends GenericWorkflow {

    public void initMission(long requesterId, long processId, String attachmentsKey, WFMission wfMission) throws BusinessException {
	String subject = "Requester: " + requesterId + ", Destination: " + wfMission.getDestination();

	initWFInstance(processId, requesterId, subject, attachmentsKey, BasicUtil.convertObjectToList(requesterId), wfMission);
    }

    protected void validateWFMission(Object... wfData) throws BusinessException {
	WFMission wfMission = (WFMission) wfData[0];

	if (wfMission.getDestination() == null)
	    throw new BusinessException(ErrorMessageCodesEnum.WF_MISSION_DESTINATION_MANDATORY.getValue());
    }

    protected void saveWFMission(Object... wfData) throws BusinessException {
	try {
	    WFMission wfMission = (WFMission) wfData[0];
	    wfMission.setInstanceId(((WFInstance) wfData[1]).getId());

	    repositoryManager.insertEntity(wfMission, (Long) wfData[2]);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}

    }
}
