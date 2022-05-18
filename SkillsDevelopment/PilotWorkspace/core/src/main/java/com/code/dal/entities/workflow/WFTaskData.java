package com.code.dal.entities.workflow;

import java.util.Date;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.BaseEntity;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.QueryConfigConstants;
import com.code.util.MultiChronologyCalendarUtil;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.WF_TaskData_GetTasksData,
		query = " select t from WFTaskData t, WFInstanceBeneficiary b " +
			" where b.instanceId = t.instanceId " +
			"   and b.beneficiaryId = (select max(ib.beneficiaryId) from WFInstanceBeneficiary ib where ib.instanceId = t.instanceId and (:P_BENEFICIARY_ID = :P_ESC_SEARCH_LONG or ib.beneficiaryId = :P_BENEFICIARY_ID)) " +
			"   and t.assigneeId = :P_ASSIGNEE_ID " +
			"   and (:P_REQUESTER_ID = :P_ESC_SEARCH_LONG or t.requesterId = :P_REQUESTER_ID) " +
			"   and (:P_PROCESS_GROUP_ID = :P_ESC_SEARCH_LONG or t.processGroupId = :P_PROCESS_GROUP_ID) " +
			"   and (:P_PROCESS_ID = :P_ESC_SEARCH_LONG or t.processId = :P_PROCESS_ID) " +
			"   and ((:P_RUNNING = 1 and t.action is null) or (:P_RUNNING = 0 and t.action is not null))" +
			"   and ((:P_NOTIFICATION_FLAG = :P_ESC_SEARCH_INT) or (:P_NOTIFICATION_FLAG = 1 and t.assigneeRole = :P_NOTIFICATION_ROLE) or (:P_NOTIFICATION_FLAG = 0 and t.assigneeRole <> :P_NOTIFICATION_ROLE)) " +
			"   and (:P_SUBJECT = :P_ESC_SEARCH_STR or t.subject like :P_SUBJECT) " +
			"   and (:P_FLAG_GROUP = :P_ESC_SEARCH_STR or t.flagGroup = :P_FLAG_GROUP) " +
			"   and t.moduleId = :P_MODULE_ID " +
			" order by t.assignmentDate "),

	@NamedQuery(
		name = QueryConfigConstants.WF_TaskData_GetTaskDataById,
		query = " select t from WFTaskData t " +
			" where t.id = :P_ID "),

	@NamedQuery(
		name = QueryConfigConstants.WF_TaskData_GetInstanceTasksData,
		query = " select t from WFTaskData t " +
			" where t.instanceId = :P_INSTANCE_ID " +
			" order by t.assignmentDate "),

	@NamedQuery(
		name = QueryConfigConstants.WF_TaskData_GetInstancePreviousTasksData,
		query = " select t from WFTaskData t " +
			" where t.instanceId = :P_INSTANCE_ID" +
			"   and t.action is not null " +
			"   and t.assigneeRole <> :P_NOTIFICATION_ROLE " +
			"   and t.id < :P_ID " +
			"   and (:P_LEVELS_FLAG = :P_ESC_SEARCH_INT or (:P_LEVELS_FLAG = 1 and t.hLevel in (:P_LEVELS))) " +
			" order by t.assignmentDate ")
})

@Data
@Entity
@Table(name = "WF_VW_TASKS")
public class WFTaskData implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "INSTANCE_ID")
    private Long instanceId;

    @Column(name = "PROCESS_ID")
    private Long processId;

    @Column(name = "PROCESS_NAME")
    private String processName;

    @Column(name = "PROCESS_GROUP_ID")
    private Long processGroupId;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "REQUESTER_ID")
    private Long requesterId;

    @Column(name = "REQUESTER_NAME")
    private String requesterName;

    @Column(name = "SUBJECT")
    private String subject;

    @Column(name = "ORGINAL_ID")
    private Long originalId;

    @Column(name = "ORGINAL_NAME")
    private String originalName;

    @Column(name = "ASSIGNEE_ID")
    private Long assigneeId;

    @JsonbTransient
    @Column(name = "ASSIGNMENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignmentDate;

    @JsonbTransient
    @Column(name = "ASSIGNMENT_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignmentHijriDate;

    @Column(name = "ASSIGNEE_ROLE")
    private String assigneeRole;

    @Column(name = "URL")
    private String url;

    @Column(name = "ACTION")
    private String action;

    @JsonbTransient
    @Column(name = "ACTION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;

    @JsonbTransient
    @Column(name = "ACTION_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionHijriDate;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "REFUSE_REASONS")
    private String refuseReasons;

    @Column(name = "FIRST_FLEX_FIELD")
    private String firstFlexField;

    @Column(name = "SECOND_FLEX_FIELD")
    private String secondFlexField;

    @Column(name = "THIRD_FLEX_FIELD")
    private String thirdFlexField;

    @Column(name = "ATTACHMENTS_KEY")
    private String attachmentsKey;

    @Column(name = "FLAG_GROUP")
    private String flagGroup;

    @Column(name = "HLEVEL")
    private String hLevel;

    public Long getDelegatorId() {
	return originalId.equals(assigneeId) ? null : originalId;
    }

    public String getDelegatorName() {
	return originalId.equals(assigneeId) ? null : originalName;
    }

    public String getAssignmentDateString() {
	return MultiChronologyCalendarUtil.getDateString(assignmentDate, ChronologyTypesEnum.GREGORIAN);
    }

    public String getAssignmentHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(assignmentHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getActionDateString() {
	return MultiChronologyCalendarUtil.getDateString(actionDate, ChronologyTypesEnum.GREGORIAN);
    }

    public String getActionHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(actionHijriDate, ChronologyTypesEnum.HIJRI);
    }
}