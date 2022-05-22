package com.code.workflow;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.CustomSession;
import com.code.dal.RepositoryManager;
import com.code.dal.entities.workflow.WFDelegation;
import com.code.dal.entities.workflow.WFDelegationData;
import com.code.dal.entities.workflow.WFInstance;
import com.code.dal.entities.workflow.WFInstanceBeneficiary;
import com.code.dal.entities.workflow.WFInstanceData;
import com.code.dal.entities.workflow.WFProcess;
import com.code.dal.entities.workflow.WFProcessGroup;
import com.code.dal.entities.workflow.WFTask;
import com.code.dal.entities.workflow.WFTaskData;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.ErrorMessageCodesEnum;
import com.code.enums.FlagsEnum;
import com.code.enums.QueryConfigConstants;
import com.code.enums.SeparatorsEnum;
import com.code.enums.WFInstanceStatusesEnum;
import com.code.enums.WFTaskActionsEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;
import com.code.util.ConfigurationUtil;
import com.code.util.ExceptionUtil;
import com.code.util.MultiChronologyCalendarUtil;

@Service
public class BaseWorkflow {

    @Autowired
    private RepositoryManager repositoryManager;

    // ------------------------------------ Instance Methods -----------------------------------
    protected WFInstance addWFInstance(long processId, long requesterId, String subject, Date requestDate, int status, String attachmentsKey, List<Long> beneficiariesIds, long transactionUserId) throws BusinessException {
	try {
	    WFInstance instance = new WFInstance();
	    instance.setProcessId(processId);
	    instance.setRequesterId(requesterId);
	    instance.setSubject(subject);
	    instance.setRequestDate(requestDate);
	    instance.setRequestHijriDate(MultiChronologyCalendarUtil.convertDate(requestDate, ChronologyTypesEnum.GREGORIAN, ChronologyTypesEnum.HIJRI));
	    instance.setStatus(status);
	    instance.setAttachmentsKey(attachmentsKey);

	    repositoryManager.insertEntity(instance, transactionUserId);
	    manageWFInstanceBeneficiaries(instance.getId(), beneficiariesIds, true, false, transactionUserId);

	    return instance;
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    protected void modifyWFInstanceSubject(WFInstance instance, String newSubject, long transactionUserId) throws BusinessException {
	try {
	    instance.setSubject(newSubject);

	    repositoryManager.updateEntity(instance, transactionUserId);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    protected void modifyWFInstanceAttachments(WFInstance instance, String attachmentsKey, long transactionUserId) throws BusinessException {
	try {
	    instance.setAttachmentsKey(attachmentsKey);

	    repositoryManager.updateEntity(instance, transactionUserId);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    protected void finalizeWFInstanceByAction(WFInstance instance, WFTask task, String action, Long[] notifiersIds, long transactionUserId) throws BusinessException {
	try {
	    Set<Long> notifiersIdsSet = new HashSet<Long>();

	    BasicUtil.addArrayToSet(notifiersIdsSet, notifiersIds);

	    List<WFTask> wfTasks = getWFInstanceTasks(instance.getId(), null);
	    wfTasks.forEach(wfTask -> notifiersIdsSet.add(wfTask.getOriginalId()));

	    changeWFInstanceStatus(instance, WFInstanceStatusesEnum.DONE.getValue(), action, transactionUserId);

	    Date curGregDate = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN);

	    notifiersIdsSet.remove(instance.getRequesterId());
	    int subHLevel = notifiersIdsSet.size() > 0 ? 1 : 0;

	    completeWFTask(task, action, curGregDate, instance.getRequesterId(), instance.getRequesterId(), WFTaskRolesEnum.NOTIFICATION.getValue(), task.getHLevel() + (subHLevel == 0 ? "" : (SeparatorsEnum.DOT.getValue() + (subHLevel++))), transactionUserId);

	    for (Long notifierId : notifiersIdsSet)
		addWFTask(instance.getId(), getDelegate(notifierId, instance.getProcessId()), notifierId, curGregDate, task.getUrl(), WFTaskRolesEnum.NOTIFICATION.getValue(), task.getHLevel() + SeparatorsEnum.DOT.getValue() + (subHLevel++), transactionUserId);

	} catch (Exception e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    public void closeWFInstanceByNotification(long instanceId, long taskId, long transactionUserId) throws BusinessException {
	WFInstance instance = getWFInstanceById(instanceId);
	WFTask task = getWFTaskById(taskId);
	closeWFInstance(instance, task, WFTaskActionsEnum.NOTIFIED.getValue(), MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN), transactionUserId);
    }

    private void closeWFInstance(WFInstance instance, WFTask task, String action, Date actionDate, long transactionUserId, CustomSession... useSession) throws BusinessException {
	try {
	    repositoryManager.beginTransaction();

	    setWFTaskAction(task, action, actionDate, transactionUserId);

	    if (countWFInstanceTasks(instance.getId()) == 1)
		changeWFInstanceStatus(instance, WFInstanceStatusesEnum.COMPLETED.getValue(), null, transactionUserId);

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    private void changeWFInstanceStatus(WFInstance instance, int newStatus, String lastAction, long transactionUserId) throws BusinessException {
	try {
	    instance.setStatus(newStatus);

	    if (lastAction != null)
		instance.setLastAction(lastAction);

	    repositoryManager.updateEntity(instance, transactionUserId);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    public WFInstance getWFInstanceById(long instanceId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(WFInstance.class, QueryConfigConstants.WF_Instance_GetInstanceById, QueryConfigConstants.WF_Instance_GetInstanceById_Params, instanceId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public WFInstanceData getWFInstanceDataById(long instanceId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(WFInstanceData.class, QueryConfigConstants.WF_InstanceData_GetInstanceDataById, QueryConfigConstants.WF_InstanceData_GetInstanceDataById_Params, instanceId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public List<WFInstanceData> getWFInstancesData(Long requesterId, Long beneficiaryId, String subject, Long processGroupId, Long processId, boolean isRunning, boolean isASC) throws BusinessException {
	if (requesterId == null && beneficiaryId == null)
	    throw new BusinessException(ErrorMessageCodesEnum.WF_REQUESTER_OR_BENEFICIARY_MANDATORY.getValue());

	try {
	    List<WFInstanceData> instances = repositoryManager.getEntities(WFInstanceData.class, QueryConfigConstants.WF_InstanceData_GetInstancesData, QueryConfigConstants.WF_InstanceData_GetInstancesData_Params,
		    BasicUtil.getValueOrEscape(requesterId), BasicUtil.getValueOrEscape(beneficiaryId), BasicUtil.getValueOrEscape(processGroupId), BasicUtil.getValueOrEscape(processId), BasicUtil.getValueLikeOrEscape(subject),
		    isRunning ? new Integer[] { WFInstanceStatusesEnum.RUNNING.getValue() } : new Integer[] { WFInstanceStatusesEnum.DONE.getValue(), WFInstanceStatusesEnum.COMPLETED.getValue() });

	    if (isASC)
		Collections.reverse(instances);

	    return instances;
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    // ------------------------------------ Instance Beneficiaries Methods ---------------------
    protected void manageWFInstanceBeneficiaries(long instanceId, List<Long> beneficiariesIds, boolean addOnly, boolean deleteOnly, long transactionUserId) throws BusinessException {
	if (BasicUtil.isNullOrEmpty(beneficiariesIds) || (addOnly && deleteOnly))
	    throw new BusinessException(ErrorMessageCodesEnum.GENERAL.getValue());

	try {
	    Map<Long, WFInstanceBeneficiary> oldInstanceBeneficiariesMap = new HashMap<Long, WFInstanceBeneficiary>();

	    if (!addOnly && !deleteOnly) {
		List<WFInstanceBeneficiary> oldInstanceBeneficiaries = getWFInstanceBeneficiariesByInstanceId(instanceId);
		oldInstanceBeneficiaries.forEach(oldBeneficiary -> oldInstanceBeneficiariesMap.put(oldBeneficiary.getBeneficiaryId(), oldBeneficiary));
	    }

	    for (Long beneficiaryId : beneficiariesIds) {
		if (!deleteOnly) {
		    if (!oldInstanceBeneficiariesMap.containsKey(beneficiaryId)) {
			WFInstanceBeneficiary wfInstanceBeneificary = new WFInstanceBeneficiary();
			wfInstanceBeneificary.setInstanceId(instanceId);
			wfInstanceBeneificary.setBeneficiaryId(beneficiaryId);
			repositoryManager.insertEntity(wfInstanceBeneificary, transactionUserId);
		    } else {
			oldInstanceBeneficiariesMap.remove(beneficiaryId);
		    }
		} else {
		    WFInstanceBeneficiary wfInstanceBeneificary = new WFInstanceBeneficiary();
		    wfInstanceBeneificary.setInstanceId(instanceId);
		    wfInstanceBeneificary.setBeneficiaryId(beneficiaryId);
		    repositoryManager.deleteEntity(wfInstanceBeneificary, transactionUserId);
		}
	    }

	    for (Long removedInstanceBeneficiaryId : oldInstanceBeneficiariesMap.keySet())
		repositoryManager.deleteEntity(oldInstanceBeneficiariesMap.get(removedInstanceBeneficiaryId), transactionUserId);

	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    private List<WFInstanceBeneficiary> getWFInstanceBeneficiariesByInstanceId(long instanceId) throws BusinessException {
	try {
	    return repositoryManager.getEntities(WFInstanceBeneficiary.class, QueryConfigConstants.WF_InstanceBeneficiary_GetBeneficiariesByInstanceId, QueryConfigConstants.WF_InstanceBeneficiary_GetBeneficiariesByInstanceId_Params, instanceId);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    // ------------------------------------ Task Methods ---------------------------------------
    protected WFTask addWFTask(long instanceId, long assigneeId, long originalId, Date assignmentDate, String taskUrl, String assigneeRole, String hLevel, long transactionUserId) throws BusinessException {
	try {
	    WFTask task = new WFTask();
	    task.setInstanceId(instanceId);
	    task.setAssigneeId(assigneeId);
	    task.setOriginalId(originalId);
	    task.setAssignmentDate(assignmentDate);
	    task.setAssignmentHijriDate(MultiChronologyCalendarUtil.convertDate(assignmentDate, ChronologyTypesEnum.GREGORIAN, ChronologyTypesEnum.HIJRI));
	    task.setUrl(taskUrl);
	    task.setAssigneeRole(assigneeRole);
	    task.setHLevel(hLevel);

	    task.setPreventAuditFlag(true);
	    repositoryManager.insertEntity(task, transactionUserId);

	    return task;
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    protected void setWFTaskAction(WFTask task, String action, Date actionDate, long transactionUserId) throws BusinessException {
	try {
	    task.setAction(action);
	    task.setActionDate(actionDate);
	    task.setActionHijriDate(MultiChronologyCalendarUtil.convertDate(actionDate, ChronologyTypesEnum.GREGORIAN, ChronologyTypesEnum.HIJRI));

	    task.setPreventAuditFlag(true);
	    repositoryManager.updateEntity(task, transactionUserId);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    protected WFTask completeWFTask(WFTask curTask, String action, Date actionDate, long assigneeId, long originalId, String assigneeRole, String hLevel, long transactionUserId, CustomSession... useSession) throws BusinessException {
	try {
	    repositoryManager.beginTransaction();

	    setWFTaskAction(curTask, action, actionDate, transactionUserId);
	    WFTask newTask = addWFTask(curTask.getInstanceId(), assigneeId, originalId, actionDate, curTask.getUrl(), assigneeRole, hLevel, transactionUserId);

	    repositoryManager.commitTransaction();
	    return newTask;
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    public void groupWFTasks(List<Long> tasksIds, String flagGroup, long transactionUserId) throws BusinessException {
	if (BasicUtil.isNullOrEmpty(tasksIds))
	    throw new BusinessException(ErrorMessageCodesEnum.WF_TASKS_MANDATORY.getValue());

	try {
	    repositoryManager.beginTransaction();

	    List<WFTask> tasks = searchWFTasksByIds(tasksIds);
	    for (WFTask task : tasks) {
		task.setFlagGroup(flagGroup);
		repositoryManager.updateEntity(task, transactionUserId);
	    }

	    repositoryManager.commitTransaction();
	} catch (RepositoryException e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    public void delegateWFTasks(List<Long> tasksIds, long delegatorId, long delegateeId, long transactionUserId) throws BusinessException {
	validateWFTasksDelegation(tasksIds, delegatorId, delegateeId);

	try {
	    repositoryManager.beginTransaction();

	    List<WFTask> tasks = searchWFTasksByIds(tasksIds);
	    for (WFTask task : tasks) {
		task.setAssigneeId(delegateeId);
		repositoryManager.updateEntity(task, transactionUserId);
	    }

	    repositoryManager.commitTransaction();
	} catch (RepositoryException e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    private void validateWFTasksDelegation(List<Long> tasksIds, long delegatorId, long delegateId) throws BusinessException {
	if (BasicUtil.isNullOrEmpty(tasksIds))
	    throw new BusinessException(ErrorMessageCodesEnum.WF_TASKS_MANDATORY.getValue());

	if (delegatorId == delegateId)
	    throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATOR_CANNOT_BE_THE_SAME_AS_DELEGATE.getValue());
    }

    protected void validateWFTaskRefuseReasonsAndNotes(WFTaskActionsEnum action, String refuseReasons, String notes) throws BusinessException {
	if (action.equals(WFTaskActionsEnum.REJECT) && BasicUtil.isNullOrEmpty(refuseReasons)) {
	    throw new BusinessException(ErrorMessageCodesEnum.WF_TASK_REFUSE_REASONS_MANDATORY.getValue());
	} else {
	    if (!BasicUtil.isNullOrEmpty(refuseReasons))
		throw new BusinessException(ErrorMessageCodesEnum.WF_TASK_REFUSE_REASONS_SHOULD_BE_EMPTY.getValue());

	    if (action.equals(WFTaskActionsEnum.RETURN_TO_REVIEWER) && BasicUtil.isNullOrEmpty(notes))
		throw new BusinessException(ErrorMessageCodesEnum.WF_TASK_NOTES_MANDATORY.getValue());
	}
    }

    public WFTask getWFTaskById(long taskId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(WFTask.class, QueryConfigConstants.WF_Task_GetTaskById, QueryConfigConstants.WF_Task_GetTaskById_Params, taskId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    private List<WFTask> searchWFTasksByIds(List<Long> tasksIds) throws BusinessException {
	try {
	    return repositoryManager.getEntities(WFTask.class, QueryConfigConstants.WF_Task_GetTasksByIds, QueryConfigConstants.WF_Task_GetTasksByIds_Params, BasicUtil.convertListToArray(tasksIds));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    protected List<WFTask> getWFInstanceTasks(long instanceId, String role) throws BusinessException {
	try {
	    return repositoryManager.getEntities(WFTask.class, QueryConfigConstants.WF_Task_GetInstanceTasks, QueryConfigConstants.WF_Task_GetInstanceTasks_Params, instanceId, BasicUtil.getValueOrEscape(role));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public WFTaskData getWFTaskDataById(long taskId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(WFTaskData.class, QueryConfigConstants.WF_TaskData_GetTaskDataById, QueryConfigConstants.WF_TaskData_GetTaskDataById_Params, taskId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public List<WFTaskData> getWFTasksData(Long assigneeId, Long requesterId, Long beneficiaryId, String subject, Long processGroupId, Long processId, boolean isRunning, Integer notificationFlag, String flagGroup, boolean isDESC) throws BusinessException {
	if (assigneeId == null && requesterId == null && beneficiaryId == null)
	    throw new BusinessException(ErrorMessageCodesEnum.WF_REQUESTER_OR_BENEFICIARY_OR_ASSIGNEE_MANDATORY.getValue());

	try {
	    List<WFTaskData> tasks = repositoryManager.getEntities(WFTaskData.class, QueryConfigConstants.WF_TaskData_GetTasksData, QueryConfigConstants.WF_TaskData_GetTasksData_Params,
		    BasicUtil.getValueOrEscape(assigneeId), BasicUtil.getValueOrEscape(requesterId), BasicUtil.getValueOrEscape(beneficiaryId), BasicUtil.getValueOrEscape(processGroupId), BasicUtil.getValueOrEscape(processId), BasicUtil.getValueLikeOrEscape(subject),
		    isRunning ? FlagsEnum.ON.getValue() : FlagsEnum.OFF.getValue(), BasicUtil.getValueOrEscape(notificationFlag), WFTaskRolesEnum.NOTIFICATION.getValue(), BasicUtil.getValueOrEscape(flagGroup));

	    if (isDESC)
		Collections.reverse(tasks);

	    return tasks;
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public Long countWFTasksData(long assigneeId, Integer notificationFlag) throws BusinessException {
	try {
	    return repositoryManager.getEntities(Long.class, QueryConfigConstants.WF_Task_CountAssigneeTasks, QueryConfigConstants.WF_Task_CountAssigneeTasks_Params, assigneeId, BasicUtil.getValueOrEscape(notificationFlag), WFTaskRolesEnum.NOTIFICATION.getValue()).get(0);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    private Long countWFInstanceTasks(long instanceId) throws BusinessException {
	try {
	    return repositoryManager.getEntities(Long.class, QueryConfigConstants.WF_Task_CountInstanceTasks, QueryConfigConstants.WF_Task_CountInstanceTasks_Params, instanceId).get(0);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public List<WFTaskData> getWFInstanceTasksData(long instanceId) throws BusinessException {
	try {
	    return repositoryManager.getEntities(WFTaskData.class, QueryConfigConstants.WF_TaskData_GetInstanceTasksData, QueryConfigConstants.WF_TaskData_GetInstanceTasksData_Params, instanceId);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public List<WFTaskData> getWFInstancePreviousCompletedTasksData(long instanceId, long taskId, String hLevel) throws BusinessException {
	try {
	    int hLevelsFlag = BasicUtil.getEscapeInteger();
	    String hLevels = BasicUtil.getEscapeString();

	    if (hLevel != null) {
		hLevelsFlag = FlagsEnum.ON.getValue();
		hLevels = hLevel;

		String tempHLevel = hLevel;
		while (tempHLevel.indexOf(SeparatorsEnum.DOT.getValue()) != -1) {
		    tempHLevel = tempHLevel.substring(0, tempHLevel.lastIndexOf(SeparatorsEnum.DOT.getValue()));
		    hLevels += SeparatorsEnum.COMMA.getValue() + tempHLevel;
		}
	    }

	    return repositoryManager.getEntities(WFTaskData.class, QueryConfigConstants.WF_TaskData_GetInstancePreviousTasksData, QueryConfigConstants.WF_TaskData_GetInstancePreviousTasksData_Params,
		    instanceId, WFTaskRolesEnum.NOTIFICATION.getValue(), taskId, hLevelsFlag, BasicUtil.getSeparatedValues(SeparatorsEnum.COMMA.getValue(), hLevels));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    // ------------------------------------ Group / Process Methods ----------------------------
    public List<WFProcessGroup> getWFProcessesGroups() throws BusinessException {
	try {
	    return repositoryManager.getEntities(WFProcessGroup.class, QueryConfigConstants.WF_ProcessGroup_GetProcessGroups, null);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public List<WFProcess> getWFProcesses(Long processGroupId, Long processId) throws BusinessException {
	try {
	    return repositoryManager.getEntities(WFProcess.class, QueryConfigConstants.WF_Process_GetProcesses, QueryConfigConstants.WF_Process_GetProcesses_Params, BasicUtil.getValueOrEscape(processId), BasicUtil.getValueOrEscape(processGroupId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    // ------------------------------------ Delegation Methods ---------------------------------
    public void saveWFDelegation(long delegatorId, long delegateId, Long processId, long transactionUserId) throws BusinessException {
	validateWFDelegation(delegatorId, delegateId, processId, transactionUserId);

	WFDelegation delegation = new WFDelegation();
	delegation.setModuleId(ConfigurationUtil.getModuleId());
	delegation.setDelegatorId(delegatorId);
	delegation.setDelegateId(delegateId);
	delegation.setProcessId(processId);

	try {
	    repositoryManager.insertEntity(delegation, transactionUserId);
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    public void deleteWFDelegation(long delegationId, long transactionUserId) throws BusinessException {
	try {
	    WFDelegation delegation = new WFDelegation();
	    delegation.setId(delegationId);
	    repositoryManager.deleteEntity(delegation, transactionUserId);
	} catch (Exception e) {
	    throw ExceptionUtil.handleException(e, transactionUserId);
	}
    }

    private void validateWFDelegation(long delegatorId, long delegateId, Long processId, long transactionUserId) throws BusinessException {
	if (delegatorId == delegateId)
	    throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATOR_CANNOT_BE_THE_SAME_AS_DELEGATE.getValue());

	// Total Delegation.
	if (processId == null) {
	    if (getWFDelegationsData(delegatorId, null, FlagsEnum.OFF.getValue(), null).size() > 0) {
		throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATOR_ALREADY_HAS_TOTAL_DELEGATION.getValue());
	    }

	    if (getWFDelegationsData(delegatorId, delegateId, FlagsEnum.ON.getValue(), null).size() > 0) {
		throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATE_ALREADY_HAS_PARTIAL_DELEGATION.getValue());
	    }
	}
	// Partial Delegation.
	else {
	    if (getWFDelegationsData(delegatorId, delegateId, FlagsEnum.OFF.getValue(), null).size() > 0) {
		throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATE_ALREADY_HAS_TOTAL_DELEGATION.getValue());
	    }

	    if (getWFDelegationsData(delegatorId, null, FlagsEnum.ON.getValue(), processId).size() > 0) {
		throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATOR_ALREADY_HAS_PARTIAL_DELEGATION.getValue());
	    }
	}
    }

    protected long getDelegate(long delegatorId, long processId) throws BusinessException {
	Set<Long> loopDetectionSet = new HashSet<Long>();
	loopDetectionSet.add(delegatorId);
	long delegateId = delegatorId;
	Long fetchedDelegateId = null;

	while (true) {
	    // Search for partial delegation.
	    fetchedDelegateId = getDelegateId(delegateId, processId);
	    if (fetchedDelegateId != null) {
		delegateId = fetchedDelegateId.longValue();
		if (!loopDetectionSet.add(delegateId))
		    throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATION_LOOP.getValue());

		continue;
	    }

	    // Search for total delegation.
	    fetchedDelegateId = getDelegateId(delegateId, null);
	    if (fetchedDelegateId != null) {
		delegateId = fetchedDelegateId.longValue();
		if (!loopDetectionSet.add(delegateId))
		    throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATION_LOOP.getValue());

		continue;
	    }

	    // No more delegations exist.
	    break;
	}

	return delegateId;
    }

    private Long getDelegateId(long delegatorId, Long processId) throws BusinessException {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(Long.class, QueryConfigConstants.WF_Delegation_GetDelegateId, QueryConfigConstants.WF_Delegation_GetDelegateId_Params, delegatorId, BasicUtil.getValueOrEscape(processId)));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }

    public List<WFDelegationData> getWFDelegationsData(Long delegatorId, Long delegateId, Integer partialFlag, Long processId) throws BusinessException {
	if (delegatorId == null && delegateId == null)
	    throw new BusinessException(ErrorMessageCodesEnum.WF_DELEGATOR_OR_DELEGATE_MANDATORY.getValue());

	try {
	    return repositoryManager.getEntities(WFDelegationData.class, QueryConfigConstants.WF_DelegationData_GetDelegationsData, QueryConfigConstants.WF_DelegationData_GetDelegationsData_Params,
		    BasicUtil.getValueOrEscape(delegatorId), BasicUtil.getValueOrEscape(delegateId), BasicUtil.getValueOrEscape(partialFlag), BasicUtil.getValueOrEscape(processId));
	} catch (RepositoryException e) {
	    throw ExceptionUtil.handleException(e, null);
	}
    }
}