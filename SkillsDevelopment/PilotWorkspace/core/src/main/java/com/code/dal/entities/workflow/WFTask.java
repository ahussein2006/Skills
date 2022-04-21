package com.code.dal.entities.workflow;

import java.util.Date;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import com.code.dal.entities.base.AuditeeEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.WF_Task_GetInstanceTasks,
		query = " select t from WFTask t " +
			" where t.instanceId = :P_INSTANCE_ID " +
			"   and (:P_ASSIGNEE_ROLE = :P_ESC_SEARCH_STR or t.assigneeRole = :P_ASSIGNEE_ROLE) " +
			" order by t.assignmentDate "),
	@NamedQuery(
		name = QueryConfigConstants.WF_Task_GetTaskById,
		query = " select t from WFTask t " +
			" where t.id = :P_ID "),

	@NamedQuery(
		name = QueryConfigConstants.WF_Task_GetTasksByIds,
		query = " select t from WFTask t " +
			" where t.id in (:P_IDS) " +
			" order by t.id "),

	@NamedQuery(
		name = QueryConfigConstants.WF_Task_CountInstanceTasks,
		query = " select count(t.id) from WFTask t " +
			" where t.instanceId = :P_INSTANCE_ID " +
			"   and t.action is null"),

	@NamedQuery(
		name = QueryConfigConstants.WF_Task_CountAssigneeTasks,
		query = " select count(t.id) from WFTask t, WFInstance i, WFProcess p, WFProcessGroup pg " +
			" where t.instanceId = i.id " +
			"   and i.processId = p.id " +
			"   and p.processGroupId = pg.id " +
			"   and pg.moduleId = :P_MODULE_ID " +
			"   and t.assigneeId = :P_ASSIGNEE_ID " +
			"   and ((:P_NOTIFICATION_FLAG = P_ESC_SEARCH_INT) " +
			"     or (:P_NOTIFICATION_FLAG = 1 and t.assigneeRole = :P_NOTIFICATION_ROLE) " +
			"     or (:P_NOTIFICATION_FLAG = 0 and t.assigneeRole <> :P_NOTIFICATION_ROLE)) " +
			"   and t.action is null"),

})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_TASKS")
public class WFTask extends AuditeeEntity {
    @SequenceGenerator(name = "WFInstanceTaskSeq", sequenceName = "WF_INSTANCE_TASK_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "WFInstanceTaskSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "INSTANCE_ID")
    private Long instanceId;

    @Column(name = "ORGINAL_ID")
    private Long originalId;

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

    @Version
    @Column(name = "VERSION")
    private Long version;

    // TODO: MultiCoronolgy Date
    public String getAssignmentDateString() {
	return "06/04/2022";
    }

    // TODO: MultiCoronolgy Date
    public String getAssignmentHijriDateString() {
	return "10/09/1443";
    }

    // TODO: MultiCoronolgy Date
    public String getActionDateString() {
	return "06/04/2022";
    }

    // TODO: MultiCoronolgy Date
    public String getActionHijriDateString() {
	return "10/09/1443";
    }

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}