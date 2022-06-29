package com.code.dal.entities.missions;

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
@Table(name = "HCM_VW_MSN_DETAILS")
public class MissionDetailData implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MISSION_ID")
    private Long missionId;

    @Column(name = "MISSION_LOCATION_FLAG")
    private Integer missionLocationFlag;

    @Column(name = "MISSION_DESTINATION")
    private String missionDestination;

    @Column(name = "MISSION_PURPOSE")
    private String missionPurpose;

    @Column(name = "MISSION_DECREE_NUMBER")
    private String missionDecreeNumber;

    @JsonbTransient
    @Column(name = "MISSION_DECREE_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date missionDecreeHijriDate;

    @Column(name = "MISSION_ATTACHMENTS_KEY")
    private String missionAttachmentsKey;

    @Column(name = "MISSION_EFLAG")
    private Integer missionEflag;

    @Column(name = "EMPLOYEE_ID")
    private Long employeeId;

    @Column(name = "EMPLOYEE_NAME")
    private String employeeName;

    @Column(name = "EMPLOYEE_MILITARY_NUMBER")
    private Integer employeeMilitaryNumber;

    @Column(name = "EMLOYEE_GRADE_CODE")
    private String emloyeeGradeCode;

    @Column(name = "EMPLOYEE_PHYSICAL_REGION_CODE")
    private String employeePhysicalRegionCode;

    @JsonbTransient
    @Column(name = "EMPLOYEE_HIRING_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date employeeHiringHijriDate;

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

    @JsonbTransient
    @Column(name = "START_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startHijriDate;

    @JsonbTransient
    @Column(name = "END_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endHijriDate;

    @Column(name = "ACTUAL_PERIOD")
    private Integer actualPeriod;

    @JsonbTransient
    @Column(name = "ACTUAL_START_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualStartHijriDate;

    @JsonbTransient
    @Column(name = "ACTUAL_END_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualEndHijriDate;

    @Column(name = "EXCEPTIONAL_APPROVAL_NUMBER")
    private String exceptionalApprovalNumber;

    @JsonbTransient
    @Column(name = "EXCEPTIONAL_APPROVAL_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date exceptionalApprovalHijriDate;

    @Column(name = "ROAD_LINE")
    private String roadLine;

    @Column(name = "ABSENCE_FLAG")
    private Integer absenceFlag;

    @Column(name = "ABSENCE_REASONS")
    private String absenceReasons;

    @JsonbTransient
    @Column(name = "JOINING_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joiningHijriDate;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "CLOSING_ATTACHMENTS_KEY")
    private String closingAttachmentsKey;

    public String getMissionDecreeHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(missionDecreeHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getEmployeeHiringHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(employeeHiringHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getStartHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(startHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getEndHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(endHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getActualStartHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(actualStartHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getActualEndHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(actualEndHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getExceptionalApprovalHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(exceptionalApprovalHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getJoiningHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(joiningHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public MissionDetail getMissionDetail() {
	MissionDetail missionDetail = new MissionDetail();
	missionDetail.setId(id);
	missionDetail.setMissionId(missionId);
	missionDetail.setEmployeeId(employeeId);
	missionDetail.setDecreeEmployeeUnitFullName(decreeEmployeeUnitFullName);
	missionDetail.setDecreeEmployeePositionCode(decreeEmployeePositionCode);
	missionDetail.setDecreeEmployeePositionTitleName(decreeEmployeePositionTitleName);
	missionDetail.setDecreeEmployeeGradeName(decreeEmployeeGradeName);
	missionDetail.setBalance(balance);
	missionDetail.setPeriod(period);
	missionDetail.setRoadPeriod(roadPeriod);
	missionDetail.setStartHijriDate(startHijriDate);
	missionDetail.setEndHijriDate(endHijriDate);
	missionDetail.setActualPeriod(actualPeriod);
	missionDetail.setActualStartHijriDate(actualStartHijriDate);
	missionDetail.setActualEndHijriDate(actualEndHijriDate);
	missionDetail.setExceptionalApprovalNumber(exceptionalApprovalNumber);
	missionDetail.setExceptionalApprovalHijriDate(exceptionalApprovalHijriDate);
	missionDetail.setRoadLine(roadLine);
	missionDetail.setAbsenceFlag(absenceFlag);
	missionDetail.setAbsenceReasons(absenceReasons);
	missionDetail.setJoiningHijriDate(joiningHijriDate);
	missionDetail.setRemarks(remarks);
	missionDetail.setClosingAttachmentsKey(closingAttachmentsKey);
	return missionDetail;
    }
}
