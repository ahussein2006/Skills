package com.code.dal.entities.workflow;

import lombok.Data;

@Data
public class WFData {
    private Object wfContent;
    private WFInstance wfInstance;
    private WFTask wfTask;

    private Long processId;
    private Long requesterId;
    private String wfInstanceAttachmentsKey;

    private Long wfTaskOriginalId;
    private String wfTaskAction;
    private String wfTaskAttachmentsKey;
    private String[] wfTaskFlexValues;
    private String[] newWFTaskFlexValues;
    private String newWFTaskRole;

    public WFData(Object wfContent, Long processId, Long requesterId, Long wfTaskOriginalId) {
	this.wfContent = wfContent;
	this.processId = processId;
	this.requesterId = requesterId;
	this.wfTaskOriginalId = wfTaskOriginalId;
    }

    public WFData(Object wfContent, Long processId, Long requesterId, String wfInstanceAttachmentsKey, String[] newWFTaskFlexValues) {
	this.wfContent = wfContent;
	this.processId = processId;
	this.requesterId = requesterId;
	this.wfInstanceAttachmentsKey = wfInstanceAttachmentsKey;
	this.newWFTaskFlexValues = newWFTaskFlexValues;
    }

    public WFData(Object wfContent, WFInstance wfInstance, String[] newWFTaskFlexValues) {
	this.wfContent = wfContent;
	this.wfInstance = wfInstance;
	this.processId = wfInstance.getProcessId();
	this.requesterId = wfInstance.getRequesterId();
	this.wfInstanceAttachmentsKey = wfInstance.getAttachmentsKey();
	this.newWFTaskFlexValues = newWFTaskFlexValues;
    }

    public WFData(Object wfContent, WFInstance wfInstance, WFTask wfTask, String wfTaskAction, String[] newWFTaskFlexValues, String newWFTaskRole) {
	this.wfContent = wfContent;
	this.wfInstance = wfInstance;
	this.wfTask = wfTask;

	this.processId = wfInstance.getProcessId();
	this.requesterId = wfInstance.getRequesterId();
	this.wfInstanceAttachmentsKey = wfInstance.getAttachmentsKey();

	this.wfTaskOriginalId = wfTask.getOriginalId();
	this.wfTaskAction = wfTaskAction == null ? wfTask.getAction() : wfTaskAction;
	this.wfTaskAttachmentsKey = wfTask.getAttachmentsKey();
	this.wfTaskFlexValues = new String[] { wfTask.getFirstFlexField(), wfTask.getSecondFlexField(), wfTask.getThirdFlexField() };
	this.newWFTaskFlexValues = newWFTaskFlexValues;
	this.newWFTaskRole = newWFTaskRole;
    }
}
