package com.code.dal.entities.workflow.missions;

import java.util.Date;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.AuditeeEntity;
import com.code.enums.ChronologyTypesEnum;
import com.code.util.MultiChronologyCalendarUtil;

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

    @Column(name = "DIRECTED_TO_JOB_NAME")
    private String directedToJobName;

    @Column(name = "CATEGORY_MODE")
    private String categoryMode;

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
	return instanceId.toString();
    }
}
