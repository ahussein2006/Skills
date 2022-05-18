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

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.WF_InstanceData_GetInstancesData,
		query = " select i from WFInstanceData i, WFInstanceBeneficiary b " +
			" where b.instanceId = i.id " +
			"   and b.beneficiaryId = (select max(ib.beneficiaryId) from WFInstanceBeneficiary ib where ib.instanceId = i.id and (:P_BENEFICIARY_ID = :P_ESC_SEARCH_LONG or ib.beneficiaryId = :P_BENEFICIARY_ID)) " +
			"   and (:P_REQUESTER_ID = :P_ESC_SEARCH_LONG or i.requesterId = :P_REQUESTER_ID) " +
			"   and (:P_PROCESS_GROUP_ID = :P_ESC_SEARCH_LONG or i.processGroupId = :P_PROCESS_GROUP_ID) " +
			"   and (:P_PROCESS_ID = :P_ESC_SEARCH_LONG or i.processId = :P_PROCESS_ID) " +
			"   and (:P_SUBJECT = :P_ESC_SEARCH_STR or i.subject like :P_SUBJECT) " +
			"   and i.status in (:P_STATUSES) " +
			"   and i.moduleId = :P_MODULE_ID " +
			" order by i.requestDate desc "),

	@NamedQuery(
		name = QueryConfigConstants.WF_InstanceData_GetInstanceDataById,
		query = " select i from WFInstanceData i " +
			" where i.id = :P_ID ")
})

@Entity
@Table(name = "WF_VW_INSTANCES")
public class WFInstanceData implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

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

    @JsonbTransient
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @JsonbTransient
    @Column(name = "REQUEST_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestHijriDate;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "LAST_ACTION")
    private String lastAction;

    @Column(name = "ATTACHMENTS_KEY")
    private String attachmentsKey;

    public String getRequestDateString() {
	return MultiChronologyCalendarUtil.getDateString(requestDate, ChronologyTypesEnum.GREGORIAN);
    }

    public String getRequestHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(requestHijriDate, ChronologyTypesEnum.HIJRI);
    }
}