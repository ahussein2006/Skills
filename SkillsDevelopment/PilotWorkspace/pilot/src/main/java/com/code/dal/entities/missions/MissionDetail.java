package com.code.dal.entities.missions;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.AuditeeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "HCM_MSN_DETAILS")
public class MissionDetail extends AuditeeEntity {
    @SequenceGenerator(name = "HCMMissionSeq", sequenceName = "HCM_MSN_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "HCMMissionSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MISSION_ID")
    private Long missionId;

    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "DECREE_EMPLOYEE_UNIT_FULL_NAME")
    private String decreeEmployeeUnitFullName;

    @Column(name = "DECREE_EMPLOYEE_POSITION_CODE")
    private String decreeEmployeePositionCode;

    @Column(name = "DECREE_EMPLOYEE_POSITION_TITLE_NAME")
    private String decreeEmployeePositionTitleName;

    @Column(name = "DECREE_EMPLOYEE_GRADE_NAME")
    private String decreeEmployeeGradeName;

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

    @Column(name = "ACTUAL_PERIOD")
    private Integer actualPeriod;

    @Column(name = "ACTUAL_START_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartHijriDate;

    @Column(name = "ACTUAL_END_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualEndHijriDate;

    @Column(name = "EXCEPTIONAL_APPROVAL_NUMBER")
    private String exceptionalApprovalNumber;

    @Column(name = "EXCEPTIONAL_APPROVAL_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exceptionalApprovalHijriDate;

    @Column(name = "ROAD_LINE")
    private String roadLine;

    @Column(name = "ABSENCE_FLAG")
    private Integer absenceFlag;

    @Column(name = "ABSENCE_REASONS")
    private String absenceReasons;

    @Column(name = "JOINING_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joiningHijriDate;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "CLOSING_ATTACHMENTS_KEY")
    private String closingAttachmentsKey;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
