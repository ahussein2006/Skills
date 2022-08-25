package com.code.dal.entities.workflow.missions;

import java.util.Date;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.BaseEntity;
import com.code.enums.ChronologyTypesEnum;
import com.code.util.MultiChronologyCalendarUtil;

import lombok.Data;

@Data
@JsonbNillable
@Entity
@Table(name = "WF_VW_MISSION_DETAILS")
public class WFMissionDetailData implements BaseEntity {
    @Id
    @Column(name = "INSTANCE_ID")
    private Long instanceId;

    @Id
    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "EMPLOYEE_NAME")
    private String employeeName;

    @Column(name = "EMPLOYEE_GRADE_CODE")
    private String employeeGradeCode;

    @Column(name = "EMPLOYEE_GRADE_NAME")
    private String employeeGradeName;

    @Column(name = "EMPLOYEE_POSITION_CODE")
    private String employeePositionCode;

    @Column(name = "EMPLOYEE_POSITION_TITLE_NAME")
    private String employeePositionTitleName;

    @Column(name = "EMPLOYEE_PHYSICAL_UNIT_FULL_NAME")
    private String employeePhysicalUnitFullName;

    @Column(name = "EMPLOYEE_PHYSICAL_UNIT_HKEY")
    private String employeePhysicalUnitHKey;

    @Column(name = "EMPLOYEE_PHYSICAL_REGION_CODE")
    private String employeePhysicalRegionCode;

    @JsonbTransient
    @Column(name = "EMPLOYEE_HIRING_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date employeeHiringHijriDate;

    @Column(name = "BALANCE")
    private Integer balance;

    @Column(name = "PERIOD")
    private Integer period;

    @Column(name = "ROAD_PERIOD")
    private Integer roadPeriod;

    @JsonbTransient
    @Column(name = "START_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startHijriDate;

    @JsonbTransient
    @Column(name = "END_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endHijriDate;

    @JsonbTransient
    @Column(name = "EXCEPTIONAL_APPROVAL_NUMBER")
    private String exceptionalApprovalNumber;

    @Column(name = "EXCEPTIONAL_APPROVAL_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exceptionalApprovalHijriDate;

    @Column(name = "ROAD_LINE")
    private String roadLine;

    @Column(name = "REMARKS")
    private String remarks;

    public String getEmployeeHiringHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(employeeHiringHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getStartHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(startHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getEndHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(endHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getExceptionalApprovalHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(exceptionalApprovalHijriDate, ChronologyTypesEnum.HIJRI);
    }
}