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
		name = QueryConfiguration.WF_ProcessGroup_GetProcessGroups,
		query = " select pg from WFProcessGroup pg" +
			" where pg.moduleId = :P_MODULE_ID " +
			" order by pg.name ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_PROCESS_GROUPS")
public class WFProcessGroup extends AuditeeEntity {
    @SequenceGenerator(name = "WFSeq", sequenceName = "WF_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "WFSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "NAME")
    private String name;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}