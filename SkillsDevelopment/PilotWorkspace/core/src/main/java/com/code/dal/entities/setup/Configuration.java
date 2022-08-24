package com.code.dal.entities.setup;

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
		name = QueryConfigConstants.SP_Configuration_GetConfigurations,
		query = " select c from Configuration c" +
			" where c.moduleId = :P_MODULE_ID " +
			" order by c.code")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "SP_CONFIG")
public class Configuration extends AuditeeEntity {

    @SequenceGenerator(name = "SetupSeq", sequenceName = "SP_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "SetupSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "CONFIG_VALUE")
    private String configValue;

    @Column(name = "DESCRIPTION")
    private String description;

    @Override
    public String caculateContentId() {
	return id.toString();
    }

}