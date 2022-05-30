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

import com.code.dal.entities.base.AuditeeEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.WF_ProcessStepAction_GetProcessStepActions,
		query = " select psa from WFProcessStepAction psa " +
			" where psa.stepId = :P_STEP_ID " +
			"   and (:P_ACTION = :P_ESC_SEARCH_STR or psa.action = :P_ACTION) " +
			" order by psa.id ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_PROCESS_STEP_ACTIONS")
public class WFProcessStepAction extends AuditeeEntity {
    @SequenceGenerator(name = "WFSeq", sequenceName = "WF_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "WFSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "STEP_ID")
    private Long stepId;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "PRE_METHOD")
    private String preMethod;

    @Column(name = "NEXT_STEP")
    private String nextStep;

    @Column(name = "POST_METHOD")
    private String postMethod;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}