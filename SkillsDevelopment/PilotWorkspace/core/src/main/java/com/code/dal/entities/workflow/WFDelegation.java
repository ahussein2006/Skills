package com.code.dal.entities.workflow;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.entities.QueryConfiguration;
import com.code.dal.entities.base.AuditeeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfiguration.WF_Delegation_GetDelegateId,
		query = " select d.delegateId from WFDelegation d " +
			" where d.moduleId = :P_MODULE_ID " +
			"   and d.delegatorId = :P_DELEGATOR_ID " +
			"   and ((:P_PROCESS_ID = :P_ESC_SEARCH_LONG and d.processId is null) or (:P_PROCESS_ID <> :P_ESC_SEARCH_LONG and d.processId = :P_PROCESS_ID)) ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_DELEGATIONS")
public class WFDelegation extends AuditeeEntity {
    @SequenceGenerator(name = "WFSeq", sequenceName = "WF_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "WFSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "DELEGATOR_ID")
    private Long delegatorId;

    @Column(name = "DELEGATE_ID")
    private Long delegateId;

    @Column(name = "PROCESS_ID")
    private Long processId;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}