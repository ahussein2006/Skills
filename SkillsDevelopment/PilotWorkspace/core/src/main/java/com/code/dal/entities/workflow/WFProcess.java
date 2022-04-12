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
		name = QueryConfiguration.WF_Process_GetProcesses,
		query = " select p from WFProcess p, WFProcessGroup pg " +
			" where p.processGroupId = pg.id " +
			"   and pg.moduleId = :P_MODULE_ID " +
			"   and (:P_ID = :P_ESC_SEARCH_LONG or p.id = :P_ID) " +
			"   and (:P_PROCESS_GROUP_ID = :P_ESC_SEARCH_LONG or p.processGroupId = :P_PROCESS_GROUP_ID) " +
			" order by p.processGroupId, p.name ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_PROCESSES")
public class WFProcess extends AuditeeEntity {
    @SequenceGenerator(name = "WFSeq", sequenceName = "WF_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "WFSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PROCESS_GROUP_ID")
    private Long processGroupId;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}