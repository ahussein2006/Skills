package com.code.dal.entities.workflow.missions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.code.dal.entities.base.AuditeeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_MISSIONS")
public class WFMission extends AuditeeEntity {
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
