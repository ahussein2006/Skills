package com.code.dal.entities.employees;

import java.util.Date;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
		name = QueryConfigConstants.HCM_EmployeeData_GetEmployeeDataById,
		query = " select e " +
			" from EmployeeData e " +
			" where e.id = :P_ID ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@JsonbNillable
@Entity
@Table(name = "HCM_EMPLOYEES")
public class EmployeeData extends AuditeeEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SOCIAL_NUMBER")
    private String socialNumber;

    @Column(name = "CATEGORY_CODE")
    private String categoryCode;

    @Column(name = "GRADE_CODE")
    private String gradeCode;

    @Column(name = "GRADE_NAME")
    private String gradeName;

    @Column(name = "POSITION_CODE")
    private String positionCode;

    @Column(name = "POSITION_TITLE_NAME")
    private String positionTitleName;

    @Column(name = "PHYSICAL_UNIT_TYPE_CODE")
    private String physicalUnitTypeCode;

    @Column(name = "PHYSICAL_UNIT_FULL_NAME")
    private String physicalUnitFullName;

    @Column(name = "PHYSICAL_UNIT_HKEY")
    private String physicalUnitHKey;

    @Column(name = "PHYSICAL_REGION_CODE")
    private String physicalRegionCode;

    @Column(name = "MANAGER_FLAG")
    private Integer managerFlag;

    @Column(name = "MANAGER_ID")
    private Long managerId;

    @JsonbTransient
    @Column(name = "HIRING_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hiringHijriDate;

    public String getHiringHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(hiringHijriDate, ChronologyTypesEnum.HIJRI);
    }

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
