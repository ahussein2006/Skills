package com.code.dal.entities.workflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.entities.QueryConfiguration;
import com.code.dal.entities.base.BaseEntity;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfiguration.WF_DelegationData_GetDelegationsData,
		query = " select d from WFDelegationData d" +
			" where d.moduleId = :P_MODULE_ID " +
			"   and (:P_DELEGATOR_ID = :P_ESC_SEARCH_LONG or d.delegatorId = :P_DELEGATOR_ID) " +
			"   and (:P_DELEGATE_ID = :P_ESC_SEARCH_LONG or d.delegateId = :P_DELEGATE_ID) " +
			"   and ((:P_PROCESS_FLAG = :P_ESC_SEARCH_INT) " +
			"     or (:P_PROCESS_FLAG = 0 and d.processId is null) " +
			"     or (:P_PROCESS_FLAG = 1 and d.processId is not null and (:P_PROCESS_ID = :P_ESC_SEARCH_LONG or d.processId = :P_PROCESS_ID))) " +
			" order by d.delegatorId, d.delegateId, d.processId ")
})

@Data
@Entity
@Table(name = "WF_VW_DELEGATIONS")
public class WFDelegationData implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "DELEGATOR_ID")
    private Long delegatorId;

    @Column(name = "DELEGATOR_NAME")
    private String delegatorName;

    @Column(name = "DELEGATE_ID")
    private Long delegateId;

    @Column(name = "DELEGATE_NAME")
    private String delegateName;

    @Column(name = "PROCESS_ID")
    private Long processId;

    @Column(name = "PROCESS_NAME")
    private String processName;
}