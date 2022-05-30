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
		name = QueryConfigConstants.WF_ProcessStep_GetProcessSteps,
		query = " select ps from WFProcessStep ps " +
			" where ps.processId = :P_PROCESS_ID " +
			"   and (:P_SEQ = :P_ESC_SEARCH_INT or ps.seq = :P_SEQ) " +
			"   and (:P_ROLE = :P_ESC_SEARCH_STR or ps.role = :P_ROLE) " +
			" order by ps.seq ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_PROCESS_STEPS")
public class WFProcessStep extends AuditeeEntity {
    @SequenceGenerator(name = "WFSeq", sequenceName = "WF_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "WFSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PROCESS_ID")
    private Long processId;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "SEQ")
    private Integer seq;

    @Column(name = "ASSIGNEES")
    private String assignees;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}