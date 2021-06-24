package com.code.dal.entities.config;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.entities.audit.AuditEntity;

@NamedQueries({
	@NamedQuery(name = "sp_configuration_getConfigurations", query = " select c from Configuration c" +
		" where (:P_CODE = :P_ESC_SEARCH_STR or c.code = :P_CODE) " +
		" order by c.code")
})

@Entity
@Table(name = "SP_CONFIG")
public class Configuration extends AuditEntity {
    private String id;
    private String code;
    private String configValue;
    private String description;

    @Id
    @Column(name = "ID")
    public String getId() {
	return id;
    }

    public void setId(String Id) {
	this.id = Id;
    }

    @Basic
    @Column(name = "CODE")
    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    @Basic
    @Column(name = "CONFIG_VALUE")
    public String getConfigValue() {
	return configValue;
    }

    public void setConfigValue(String configValue) {
	this.configValue = configValue;
    }

    @Basic
    @Column(name = "DESCRIPTION")
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Override
    public void setGlobalId() {
	this.id = this.getGlobalId();
    }

    @Override
    public String calculateContentId() {
	return this.getId().toString();
    }

}