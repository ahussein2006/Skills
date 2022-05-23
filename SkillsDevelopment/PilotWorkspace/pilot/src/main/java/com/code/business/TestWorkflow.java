package com.code.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.RepositoryManager;
import com.code.dal.entities.workflow.WFInstance;
import com.code.dal.entities.workflow.WFTask;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.WFInstanceStatusesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.util.BasicUtil;
import com.code.util.ExceptionUtil;
import com.code.util.MultiChronologyCalendarUtil;
import com.code.workflow.BaseWorkflow;

@Service
public class TestWorkflow extends BaseWorkflow {

    @Autowired
    private RepositoryManager repositoryManager;

    // ------------------------------------ Test Base Workflow ---------------------------------

    public void initInstance(long requesterId, long processId, String taskUrl) throws BusinessException {

	try {
	    repositoryManager.beginTransaction();

	    Date curDate = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN);
	    String subject = "Requester: " + requesterId + ", Process: " + processId;

	    WFInstance instance = addWFInstance(processId, requesterId, subject, curDate, WFInstanceStatusesEnum.RUNNING.getValue(), null, BasicUtil.convertObjectToList(requesterId), requesterId);

	    addWFTask(instance.getId(), getDelegate(2, processId), 2, curDate, taskUrl, WFTaskRolesEnum.SIGN_MANAGER.getValue(), "1", requesterId);

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, requesterId);
	}
    }

    public void reviewInstance(long reTaskId, String notes) throws BusinessException {
	WFTask reTask = getWFTaskById(reTaskId);
	WFInstance instance = getWFInstanceById(reTask.getInstanceId());

	try {
	    reTask.setNotes(notes);
	    Date curDate = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN);
	    completeWFTask(reTask, WFTaskActionsEnum.REVIEW.getValue(), curDate, getDelegate(2, instance.getProcessId()), 2, WFTaskRolesEnum.SIGN_MANAGER.getValue(), "1", reTask.getAssigneeId());

	} catch (Exception e) {
	    throw ExceptionUtil.handleException(e, reTask != null ? reTask.getAssigneeId() : null);
	}
    }

    public void signInstance(long smTaskId, String notes, String refuseReasons, String taskAction) throws BusinessException {
	validateWFTaskRefuseReasonsAndNotes(taskAction, refuseReasons, notes);
	WFTask smTask = getWFTaskById(smTaskId);
	WFInstance instance = getWFInstanceById(smTask.getInstanceId());
	smTask.setNotes(notes);
	Date curDate = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN);

	try {
	    repositoryManager.beginTransaction();

	    if (WFTaskActionsEnum.SIGN.getValue().equals(taskAction)) {
		if (smTask.getOriginalId().equals(2L))
		    completeWFTask(smTask, WFTaskActionsEnum.SIGN.getValue(), curDate, getDelegate(3, instance.getProcessId()), 3, WFTaskRolesEnum.SIGN_MANAGER.getValue(), "1", smTask.getAssigneeId());
		else
		    finalizeWFInstanceByAction(instance, smTask, WFTaskActionsEnum.SIGN.getValue(), new Long[] { 8L, 9L }, smTask.getAssigneeId());
	    } else if (WFTaskActionsEnum.RETURN_TO_REVIEWER.getValue().equals(taskAction)) {
		modifyWFInstanceSubject(instance, instance.getSubject() + ", Returned", smTask.getAssigneeId());
		completeWFTask(smTask, WFTaskActionsEnum.RETURN_TO_REVIEWER.getValue(), curDate, getDelegate(instance.getRequesterId(), instance.getProcessId()), instance.getRequesterId(), WFTaskRolesEnum.REVIEWER_EMP.getValue(), "1", smTask.getAssigneeId());
	    } else { // reject
		smTask.setRefuseReasons(refuseReasons);
		finalizeWFInstanceByAction(instance, smTask, WFTaskActionsEnum.REJECT.getValue(), null, smTask.getAssigneeId());
	    }

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, smTask != null ? smTask.getAssigneeId() : null);
	}
    }
}
