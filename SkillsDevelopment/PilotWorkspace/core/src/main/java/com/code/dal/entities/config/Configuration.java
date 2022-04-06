package com.code.dal.entities.config;

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
		name = QueryConfiguration.SP_CONFIGURATION_GET_CONFIGURATIONS,
		query = " select c from Configuration c" +
			" where (:P_CODE = :P_ESC_SEARCH_STR or c.code = :P_CODE) " +
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