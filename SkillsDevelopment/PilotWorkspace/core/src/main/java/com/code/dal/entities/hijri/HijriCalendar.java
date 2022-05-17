package com.code.dal.entities.hijri;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.BaseEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.SP_HijriCalendar_GetHijriCalendar,
		query = " select h from HijriCalendar h " +
			" order by h.hijriYear, h.hijriMonth ")
})

@Data
@Entity
@Table(name = "SP_HIJRI_CALENDAR")
public class HijriCalendar implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "HIJRI_YEAR")
    private Integer hijriYear;

    @Column(name = "HIJRI_MONTH")
    private Integer hijriMonth;

    @Column(name = "HIJRI_MONTH_LENGTH")
    private Integer hijriMonthLength;

    @Column(name = "HIJRI_MONTH_END_GREGORIAN_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date hijriMonthEndGregorianDate;
}