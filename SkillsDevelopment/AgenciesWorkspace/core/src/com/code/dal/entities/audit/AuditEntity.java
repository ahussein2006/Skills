
package com.code.dal.entities.audit;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.code.business.util.ContentUtil;
import com.code.dal.entities.BaseEntity;

public abstract class AuditEntity extends BaseEntity {
    private boolean preventAuditFlag;

    @Transient
    @XmlTransient
    @JsonbTransient
    public boolean getPreventAuditFlag() {
	return preventAuditFlag;
    }

    public void setPreventAuditFlag(boolean preventAuditFlag) {
	this.preventAuditFlag = preventAuditFlag;
    }

    public abstract String calculateContentId();

    public String calculateContent() {
	return ContentUtil.convertToJsonString(this);
    }
}