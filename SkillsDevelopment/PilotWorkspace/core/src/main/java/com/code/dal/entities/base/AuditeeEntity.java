package com.code.dal.entities.base;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Transient;

import com.code.util.ContentUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class AuditeeEntity extends CommonEntity {
    @JsonbTransient
    @Transient
    private boolean preventAuditFlag;

    public abstract String caculateContentId();

    public String calculateContent() {
	return ContentUtil.convertToJsonString(this);
    }
}