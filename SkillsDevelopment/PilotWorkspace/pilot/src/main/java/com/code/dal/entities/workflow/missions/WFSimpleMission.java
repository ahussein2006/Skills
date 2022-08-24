package com.code.dal.entities.workflow.missions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.entities.base.AuditeeEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.WF_SimpleMission_GetMissionById,
		query = " select m from WFSimpleMission m " +
			" where m.instanceId = :P_INSTANCE_ID ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_SIMPLE_MISSIONS")
public class WFSimpleMission extends AuditeeEntity {
    @Id
    @Column(name = "INSTANCE_ID")
    private Long instanceId;

    @Column(name = "DESTINATION")
    private String destination;

    @Override
    public String caculateContentId() {
	return instanceId.toString();
    }
}
