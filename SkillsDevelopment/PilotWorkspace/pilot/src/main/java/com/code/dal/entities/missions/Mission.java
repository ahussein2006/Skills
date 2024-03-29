package com.code.dal.entities.missions;

import java.util.Date;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.AuditeeEntity;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.QueryConfigConstants;
import com.code.util.MultiChronologyCalendarUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.MSN_Mission_GetMissionById,
		query = " select m " +
			" from Mission m " +
			" where m.id = :P_ID "),
	@NamedQuery(
		name = QueryConfigConstants.MSN_Mission_GetMissions,
		query = " select m " +
			" from Mission m " +
			" where (:P_LOCATION_FLAG = :P_ESC_SEARCH_INT or m.locationFlag = :P_LOCATION_FLAG) " +
			"   and (:P_DECREE_NUMBER = :P_ESC_SEARCH_STR or m.decreeNumber = :P_DECREE_NUMBER) " +
			"   and (:P_FROM_DATE_FLAG = :P_ESC_SEARCH_INT or m.startHijriDate >= to_date(:P_FROM_DATE, 'MI/MM/YYYY')) " +
			"   and (:P_TO_DATE_FLAG = :P_ESC_SEARCH_INT or m.startHijriDate <= to_date(:P_TO_DATE, 'MI/MM/YYYY')) " +
			"   and (:P_EMPLOYEE_ID = :P_ESC_SEARCH_LONG or exists (select md.id from MissionDetail md where md.missionId = m.id and md.employeeId = :P_EMPLOYEE_ID)) " +
			" order by m.startHijriDate "),
	@NamedQuery(
		name = QueryConfigConstants.MSN_Mission_GetMissionsCount,
		query = " select count(m.id) " +
			" from Mission m " +
			" where (:P_LOCATION_FLAG = :P_ESC_SEARCH_INT or m.locationFlag = :P_LOCATION_FLAG) " +
			"   and (:P_DECREE_NUMBER = :P_ESC_SEARCH_STR or m.decreeNumber = :P_DECREE_NUMBER) " +
			"   and (:P_FROM_DATE_FLAG = :P_ESC_SEARCH_INT or m.startHijriDate >= to_date(:P_FROM_DATE, 'MI/MM/YYYY')) " +
			"   and (:P_TO_DATE_FLAG = :P_ESC_SEARCH_INT or m.startHijriDate <= to_date(:P_TO_DATE, 'MI/MM/YYYY')) " +
			"   and (:P_EMPLOYEE_ID = :P_ESC_SEARCH_LONG or exists (select md.id from MissionDetail md where md.missionId = m.id and md.employeeId = :P_EMPLOYEE_ID)) " +
			" order by m.startHijriDate ")

})

@Data
@EqualsAndHashCode(callSuper = false)
@JsonbNillable
@Entity
@Table(name = "HCM_MSN_TRANSACTIONS")
public class Mission extends AuditeeEntity {
    @SequenceGenerator(name = "HCMMissionSeq", sequenceName = "HCM_MSN_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "HCMMissionSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOCATION_FLAG")
    private Integer locationFlag;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "DESTINATION")
    private String destination;

    @Column(name = "PURPOSE")
    private String purpose;

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

    @Column(name = "MINISTERY_APPROVAL_NUMBER")
    private String ministeryApprovalNumber;

    @JsonbTransient
    @Column(name = "MINISTERY_APPROVAL_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ministeryApprovalHijriDate;

    @Column(name = "ROAD_LINE")
    private String roadLine;

    @Column(name = "DOUBLE_BALANCE_FLAG")
    private Integer doubleBalanceFlag;

    @Column(name = "HAJJ_FLAG")
    private Integer hajjFlag;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "DECREE_NUMBER")
    private String decreeNumber;

    @JsonbTransient
    @Column(name = "DECREE_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date decreeHijriDate;

    @Column(name = "ATTACHMENTS_KEY")
    private String attachmentsKey;

    @Column(name = "INTERNAL_COPIES")
    private String internalCopies;

    @Column(name = "EXTERNAL_COPIES")
    private String externalCopies;

    @Column(name = "APPROVED_ID")
    private Long approvedId;

    @Column(name = "DECREE_APPROVED_ID")
    private Long decreeApprovedId;

    @Column(name = "ORIGINAL_DECREE_APPROVED_ID")
    private Long originaldecreeApprovedId;

    @Column(name = "DECREE_REGION_CODE")
    private String decreeRegionCode;

    @Column(name = "DIRECTED_TO_POSITION_TITLE")
    private String directedToPositionTitle;

    @Column(name = "CATEGORY_CODE")
    private String categoryCode;

    @Column(name = "EFLAG")
    private Integer eflag;

    @Column(name = "MIG_FLAG")
    private Integer migFlag;

    public String getStartHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(startHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getEndHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(endHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getMinisteryApprovalHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(ministeryApprovalHijriDate, ChronologyTypesEnum.HIJRI);
    }

    public String getDecreeHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(decreeHijriDate, ChronologyTypesEnum.HIJRI);
    }

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
