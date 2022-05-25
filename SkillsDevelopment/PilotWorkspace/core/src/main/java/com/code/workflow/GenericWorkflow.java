package com.code.workflow;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.code.dal.entities.workflow.WFInstance;
import com.code.dal.entities.workflow.WFProcess;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.WFInstanceStatusesEnum;
import com.code.enums.WFTaskRolesEnum;
import com.code.exceptions.BusinessException;
import com.code.util.ExceptionUtil;
import com.code.util.MultiChronologyCalendarUtil;
import com.code.util.ReflictionUtil;

@Service
public class GenericWorkflow extends BaseWorkflow {

    public void initWFInstance(long processId, long requesterId, String subject, String attachmentsKey, List<Long> beneficiariesIds, Object wfData) throws BusinessException {
	WFProcess wfProcess = getWFProcessById(processId);
	if (wfProcess.getPreInit() != null)
	    ReflictionUtil.invokeMethod(wfProcess.getPreInit(), wfData, requesterId, attachmentsKey);

	try {
	    repositoryManager.beginTransaction();

	    Date curDate = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN);

	    WFInstance wfInstance = addWFInstance(processId, requesterId, subject, curDate, WFInstanceStatusesEnum.RUNNING.getValue(), attachmentsKey, beneficiariesIds, requesterId);

	    addWFTask(wfInstance.getId(), getDelegate(2, processId), 2, curDate, wfProcess.getUrl(), WFTaskRolesEnum.SIGN_MANAGER.getValue(), "1", requesterId);

	    if (wfProcess.getPostInit() != null)
		ReflictionUtil.invokeMethod(wfProcess.getPostInit(), wfData, wfInstance, requesterId);

	    repositoryManager.commitTransaction();
	} catch (Exception e) {
	    repositoryManager.rollbackTransaction();
	    throw ExceptionUtil.handleException(e, requesterId);
	}
    }
}
