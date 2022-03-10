
package com.code.dal.entities.audit;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Transient;

import com.code.dal.entities.base.CommonEntity;
import com.code.util.ContentUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public abstract class AuditeeEntity extends CommonEntity {
	@Transient
	@JsonbTransient
	private boolean preventAuditFlag;

	public String calculateContent() {
		return ContentUtil.convertToJsonString(this);
	}
}