package com.code.dal.entities.um.audit;

import java.util.Date;

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

import com.code.dal.entities.base.BaseEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.UM_AuditLog_GetAuditLogs,
		query = " select a from AuditLog a" +
			" where (:P_CONTENT_ENTITY = :P_ESC_SEARCH_STR or a.contentEntity = :P_CONTENT_ENTITY) " +
			"   and (:P_CONTENT_ID = :P_ESC_SEARCH_STR or a.contentId = :P_CONTENT_ID)" +
			"   and (:P_OPERATION = :P_ESC_SEARCH_STR or a.operation = :P_OPERATION)" +
			"   and (:P_USER_ID= :P_ESC_SEARCH_LONG or a.userId = :P_USER_ID)" +
			"   and (:P_FROM_DATE_FLAG = :P_ESC_SEARCH_INT or TO_DATE(a.operationDate , 'DD/MM/YYYY') >= TO_DATE(:P_FROM_DATE, 'DD/MM/YYYY'))" +
			"   and (:P_TO_DATE_FLAG = :P_ESC_SEARCH_INT or TO_DATE(a.operationDate , 'DD/MM/YYYY') <= TO_DATE(:P_TO_DATE, 'DD/MM/YYYY'))" +
			"   and (:P_CONTENT = :P_ESC_SEARCH_STR or a.content like :P_CONTENT)" +
			" order by a.contentEntity, a.operationDate, a.contentId ")
})

@Data
@Entity
@Table(name = "UM_AUDIT_LOGS")
public class AuditLog implements BaseEntity {

    @SequenceGenerator(name = "AuditLogsSeq", sequenceName = "UM_AUDIT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "AuditLogsSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "OPERATION")
    private String operation;

    @JsonbTransient
    @Column(name = "OPERATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;

    @Column(name = "CONTENT_ENTITY")
    private String contentEntity;

    @Column(name = "CONTENT_ID")
    private String contentId;

    @Column(name = "CONTENT")
    private String content;

    // TODO: MultiCoronolgy Date
    public String getOperationDateString() {
	return "06/04/2022";
    }
}