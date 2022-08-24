package com.code.workflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.code.dal.entities.workflow.WFData;
import com.code.dal.entities.workflow.WFInstance;
import com.code.dal.entities.workflow.WFProcess;
import com.code.dal.entities.workflow.WFProcessStep;
import com.code.dal.entities.workflow.WFProcessStepAction;
import com.code.dal.entities.workflow.WFTask;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.QueryConfigConstants;
import com.code.enums.SeparatorsEnum;
import com.code.enums.WFInstanceStatusesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;
import com.code.util.ExceptionUtil;
import com.code.util.MultiChronologyCalendarUtil;
import com.code.util.ReflictionUtil;

@Service
public class GenericWorkflow extends BaseWorkflow {

    // ------------------------------------ Workflow Management --------------------------------
    public void initWFInstance(long processId, long requesterId, String subject, String attachmentsKey, List<Long> beneficiariesIds, Object wfContent, String[] flexValues) throws BusinessException {
	WFProcess wfProcess = getWFProcessById(processId);
	if (wfProcess.getPreInit() != null)
	    ReflictionUtil.invokeWFMethod(wfProcess.getPreInit(), new WFData(wfContent, processId, requesterId, attachmentsKey, flexValues));

	WFProcessStep wfFirstStep = getWFProcessFirstStep(wfProcess, wfContent, requesterId);
	List<Long> wfFirstStepAssignees = getWFProcessStepAssignees(wfFirstStep, wfContent, processId, requesterId, null);

	Date curDate = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN);

	try {
	    repositoryManager.beginTransaction();

	    WFInstance wfInstance = addWFInstance(processId, requesterId, subject, curDate, wfFirstStep.getRole().equals(WFTaskRolesEnum.NOTIFICATION.toString()) ? WFInstanceStatusesEnum.DONE.getValue() : WFInstanceStatusesEnum.RUNNING.getValue(), attachmentsKey, beneficiariesIds, requesterId);

	    for (int i = 0; i < wfFirstStepAssignees.size(); i++)
		addWFTask(wfInstance.getId(), getDelegate(wfFirstStepAssignees.get(i), processId), wfFirstStepAssignees.get(i), curDate, wfProcess.getUrl(), WFTaskRolesEnum.valueOf(wfFirstStep.getRole()),
			wfFirstStepAssignees.size() == 1 ? "1" : "1" + SeparatorsEnum.DOT.getValue() + (i + 1), flexValues, requesterId);

	    if (wfProcess.getPostInit() != null)
		ReflictionUtil.invokeWFMethod(wfProcess.getPostInit(), new WFData(wfContent, wfInstance, flexValues));

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, requesterId);
	}
    }

    public void doWFTaskAction(WFTask wfTask, String action, String notes, String refuseReasons, String attachmentsKey, String[] flexValues, String[] newFlexValues, Object wfContent) throws BusinessException {
	validateWFTaskRefuseReasonsAndNotes(action, refuseReasons, notes);

	Date curDate = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN);

	wfTask.setNotes(BasicUtil.getTrimmedString(notes));
	wfTask.setRefuseReasons(BasicUtil.getTrimmedString(refuseReasons));
	wfTask.setAttachmentsKey(attachmentsKey);
	setWFTaskFlexValues(wfTask, flexValues);

	WFInstance wfInstance = getWFInstanceById(wfTask.getInstanceId());

	if (wfTask.getAssigneeRole().equals(WFTaskRolesEnum.NOTIFICATION.toString()) && WFTaskActionsEnum.NOTIFIED.getValue().equals(action)) {
	    closeWFInstance(wfInstance, wfTask, action, curDate, wfTask.getAssigneeId());
	    return;
	}

	WFProcessStep wfProcessStep = getWFProcessStepByRole(wfInstance.getProcessId(), wfTask.getAssigneeRole());
	WFProcessStepAction wfProcessStepAction = getWFProcessStepActionByAction(wfProcessStep.getId(), action);

	if (wfProcessStepAction.getPreMethod() != null)
	    ReflictionUtil.invokeWFMethod(wfProcessStepAction.getPreMethod(), new WFData(wfContent, wfInstance, wfTask, action, newFlexValues, null));

	WFProcessStep wfNextProcessStep = getWFProcessNextStep(wfProcessStep, wfProcessStepAction, wfContent, wfInstance.getProcessId(), wfInstance.getRequesterId(), wfTask.getOriginalId());
	List<Long> wfNextStepAssignees = new ArrayList<Long>();

	long CurrentWFTasksCount = countWFInstanceTasks(wfInstance.getId());
	if (CurrentWFTasksCount == 1)
	    wfNextStepAssignees = getWFProcessStepAssignees(wfNextProcessStep, wfContent, wfProcessStep.getProcessId(), wfInstance.getRequesterId(), wfTask.getOriginalId());

	try {
	    repositoryManager.beginTransaction();

	    if (CurrentWFTasksCount == 1) {
		if (wfNextProcessStep.getRole().equals(WFTaskRolesEnum.NOTIFICATION.toString())) {
		    finalizeWFInstanceByAction(wfInstance, wfTask, action, BasicUtil.convertLongListToArray(wfNextStepAssignees), wfTask.getAssigneeId());
		} else {
		    completeWFTask(wfTask, action, curDate, getDelegate(wfNextStepAssignees.get(0), wfInstance.getProcessId()), wfNextStepAssignees.get(0), WFTaskRolesEnum.valueOf(wfNextProcessStep.getRole()),
			    wfNextStepAssignees.size() == 1 ? wfTask.getHLevel() : wfTask.getHLevel() + SeparatorsEnum.DOT.getValue() + "1", newFlexValues, wfTask.getAssigneeId());

		    for (int i = 1; i < wfNextStepAssignees.size(); i++)
			addWFTask(wfInstance.getId(), getDelegate(wfNextStepAssignees.get(i), wfInstance.getProcessId()), wfNextStepAssignees.get(i), curDate, wfTask.getUrl(), WFTaskRolesEnum.valueOf(wfNextProcessStep.getRole()),
				wfTask.getHLevel() + SeparatorsEnum.DOT.getValue() + (i + 1), newFlexValues, wfTask.getAssigneeId());
		}
	    } else {
		setWFTaskAction(wfTask, action, curDate, wfTask.getAssigneeId());
	    }

	    if (wfProcessStepAction.getPostMethod() != null)
		ReflictionUtil.invokeWFMethod(wfProcessStepAction.getPostMethod(), new WFData(wfContent, wfInstance, wfTask, null, newFlexValues, wfNextProcessStep.getRole()));

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, wfTask.getAssigneeId());
	}
    }

    // ------------------------------------ Process Steps --------------------------------------
    @SuppressWarnings("unchecked")
    private List<Long> getWFProcessStepAssignees(WFProcessStep wfProcessStep, Object wfContent, long processId, long requesterId, Long originalId) throws BusinessException {
	String[] assigneesDataArray = BasicUtil.getSeparatedValues(SeparatorsEnum.COMMA.getValue(), wfProcessStep.getAssignees());
	List<Long> assignees = new ArrayList<Long>();

	for (String assigneeData : assigneesDataArray) {
	    if (BasicUtil.isDigit(assigneeData)) {
		if (!assigneeData.equals("0"))
		    assignees.add(Long.parseLong(assigneeData));
	    } else {
		assignees.addAll((List<Long>) ReflictionUtil.invokeWFMethod(assigneeData, new WFData(wfContent, processId, requesterId, originalId)));
	    }
	}

	return assignees;
    }

    private WFProcessStep getWFProcessFirstStep(WFProcess wfProcess, Object wfContent, long requesterId) throws BusinessException {
	if (BasicUtil.isDigit(wfProcess.getFirstStep())) {
	    return getWFProcessStepBySeq(wfProcess.getId(), Integer.parseInt(wfProcess.getFirstStep()));
	} else {
	    String role = (String) ReflictionUtil.invokeWFMethod(wfProcess.getFirstStep(), new WFData(wfContent, wfProcess.getId(), requesterId, null));
	    return getWFProcessStepByRole(wfProcess.getId(), role);
	}
    }

    private WFProcessStep getWFProcessNextStep(WFProcessStep wfProcessStep, WFProcessStepAction wfProcessStepAction, Object wfContent, long processId, long requesterId, long originalId) throws BusinessException {
	String[] conditionsArray = BasicUtil.getSeparatedValues(SeparatorsEnum.COMMA.getValue(), wfProcessStepAction.getNextStep());

	for (String condition : conditionsArray) {
	    if (BasicUtil.isDigit(condition)) {
		int nextStepSeq = Integer.parseInt(condition);
		return wfProcessStep.getSeq().equals(nextStepSeq) ? wfProcessStep : getWFProcessStepBySeq(wfProcessStep.getProcessId(), nextStepSeq);
	    } else {
		String[] conditionParts = BasicUtil.getSeparatedValues(SeparatorsEnum.UNDERSCORE.getValue(), condition);
		boolean conditionResult = (boolean) ReflictionUtil.invokeWFMethod(conditionParts[0], new WFData(wfContent, processId, requesterId, originalId));
		if (conditionResult)
		    return getWFProcessStepBySeq(wfProcessStep.getProcessId(), Integer.parseInt(conditionParts[1]));
	    }
	}
	return null;
    }

    private WFProcessStep getWFProcessStepBySeq(long processId, int seq) throws BusinessException {
	return BasicUtil.getFirstItem(searchWFProcessSteps(processId, seq, null));
    }

    private WFProcessStep getWFProcessStepByRole(long processId, String role) throws BusinessException {
	return BasicUtil.getFirstItem(searchWFProcessSteps(processId, null, role));
    }

    public List<WFProcessStep> getWFProcessSteps(long processId) throws BusinessException {
	return searchWFProcessSteps(processId, null, null);
    }

    private List<WFProcessStep> searchWFProcessSteps(long processId, Integer seq, String role) throws BusinessException {
	try {
	    List<WFProcessStep> wfProcessSteps = repositoryManager.getEntities(WFProcessStep.class, QueryConfigConstants.WF_ProcessStep_GetProcessSteps, QueryConfigConstants.WF_ProcessStep_GetProcessSteps_Params,
		    processId, BasicUtil.getValueOrEscape(seq), BasicUtil.getValueOrEscape(role));

	    return wfProcessSteps;
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    // ------------------------------------ Process Step Actions -------------------------------
    private WFProcessStepAction getWFProcessStepActionByAction(long stepId, String action) throws BusinessException {
	return BasicUtil.getFirstItem(searchWFProcessStepActions(stepId, action));
    }

    public List<WFProcessStepAction> getWFProcessStepActions(long stepId) throws BusinessException {
	return searchWFProcessStepActions(stepId, null);
    }

    private List<WFProcessStepAction> searchWFProcessStepActions(long stepId, String action) throws BusinessException {
	try {
	    List<WFProcessStepAction> wfProcessSteps = repositoryManager.getEntities(WFProcessStepAction.class, QueryConfigConstants.WF_ProcessStepAction_GetProcessStepActions, QueryConfigConstants.WF_ProcessStepAction_GetProcessStepActions_Params,
		    stepId, BasicUtil.getValueOrEscape(action));

	    return wfProcessSteps;
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }
}
