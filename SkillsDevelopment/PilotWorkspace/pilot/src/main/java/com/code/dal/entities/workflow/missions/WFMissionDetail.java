package com.code.dal.entities.workflow.missions;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.AuditeeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "WF_MISSION_DETAILS")
public class WFMissionDetail extends AuditeeEntity {
    @Id
    @Column(name = "INSTANCE_ID")
    private Long instanceId;

    @Id
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "BALANCE")
    private Integer balance;

    @Column(name = "PERIOD")
    private Integer period;

    @Column(name = "ROAD_PERIOD")
    private Integer roadPeriod;

    @Column(name = "START_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startHijriDate;

    @Column(name = "END_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endHijriDate;

    @Column(name = "EXCEPTIONAL_APPROVAL_NUMBER")
    private String exceptionalApprovalNumber;

    @Column(name = "EXCEPTIONAL_APPROVAL_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exceptionalApprovalHijriDate;

    @Column(name = "ROAD_LINE")
    private String roadLine;

    @Column(name = "REMARKS")
    private String remarks;

    @Override
    public String caculateContentId() {
	return instanceId.toString();
    }
}
