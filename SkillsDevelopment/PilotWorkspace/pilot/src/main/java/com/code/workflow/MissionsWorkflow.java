package com.code.workflow;

import java.util.List;

import org.springframework.stereotype.Service;

import com.code.dal.entities.workflow.WFData;
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

	initWFInstance(processId, requesterId, subject, attachmentsKey, BasicUtil.convertObjectToList(requesterId), wfMission, null);
    }

    public void doMission(long taskId, String action, String notes, String refuseReasons, String attachmentsKey, WFMission wfMission) throws BusinessException {
	doWFTaskAction(getWFTaskById(taskId), action, notes, refuseReasons, attachmentsKey, null, null, wfMission);
    }

    // -----------------------------------------------------------------------------------------
    protected void validateWFMission(WFData wfData) throws BusinessException {
	WFMission wfMission = (WFMission) wfData.getWfContent();

	if (wfMission.getDestination() == null)
	    throw new BusinessException(ErrorMessageCodesEnum.WF_MISSION_DESTINATION_MANDATORY);
    }

    protected void saveWFMission(WFData wfData) throws BusinessException {
	Long transactionUserId = null;
	try {
	    WFMission wfMission = (WFMission) wfData.getWfContent();
	    WFInstance wfInstance = wfData.getWfInstance();
	    String subject = "Requester: " + wfInstance.getRequesterId() + ", Destination: " + wfMission.getDestination();

	    if (wfMission.getInstanceId() == null) {
		transactionUserId = wfInstance.getRequesterId();
		wfMission.setInstanceId(wfInstance.getId());
		repositoryManager.insertEntity(wfMission, transactionUserId);
	    } else if (!wfInstance.getSubject().equals(subject)) {
		transactionUserId = wfData.getWfTask().getAssigneeId();
		modifyWFInstanceSubject(wfInstance, subject, transactionUserId);
		repositoryManager.updateEntity(wfMission, transactionUserId);
	    }
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}

    }

    protected void approveWFMission(WFData wfData) throws BusinessException {

    }

    // -----------------------------------------------------------------------------------------
    protected boolean isFinalDM(WFData wfData) throws BusinessException {
	return wfData.getWfTaskOriginalId() == getDirectManager(getDirectManager(wfData.getRequesterId()));
    }

    protected boolean isFinalSM(WFData wfData) throws BusinessException {
	return wfData.getWfTaskOriginalId() == getDirectManager(getDirectManager(getReviewer()));
    }

    protected List<Long> getDirectManager(WFData wfData) throws BusinessException {
	long curUser = wfData.getWfTaskOriginalId() == null ? wfData.getRequesterId() : wfData.getWfTaskOriginalId();

	return BasicUtil.convertObjectToList(getDirectManager(curUser));
    }

    protected List<Long> getManagerRedirect(WFData wfData) throws BusinessException {
	return BasicUtil.convertObjectToList(getDirectManager(getReviewer()));
    }

    protected List<Long> getReviewer(WFData wfData) throws BusinessException {
	return BasicUtil.convertObjectToList(getReviewer());
    }

    protected List<Long> getSignManager(WFData wfData) throws BusinessException {
	return BasicUtil.convertObjectToList(getDirectManager(wfData.getWfTaskOriginalId()));
    }

    private long getDirectManager(long userId) {
	return ++userId;
    }

    private long getReviewer() {
	return 5L;
    }

    // -----------------------------------------------------------------------------------------
    public WFMission getWFMission(long missionId) throws BusinessException {
	return null;
    }
}
