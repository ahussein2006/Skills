package com.code.dal.entities.audit;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.code.dal.entities.base.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UM_AUDIT_LOGS")
public class AuditLog implements BaseEntity {

	@SequenceGenerator(name = "AuditLogsSeq", sequenceName = "UM_AUDIT_SEQ", allocationSize = 1)
	@GeneratedValue(generator = "AuditLogsSeq", strategy = GenerationType.SEQUENCE)
	@Id
	@Column(name = "ID")
	private Long id;

	@Basic
	@Column(name = "MODULE_ID")
	private Long moduleId;

	@Basic
	@Column(name = "USER_ID")
	private Long userId;

	@Basic
	@Column(name = "SYSTEM_NAME")
	private String systemName;

	@Basic
	@Column(name = "OPERATION")
	private String operation;

	@Basic
	@Column(name = "OPERATION_GREG_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date operationGregDate;

	@Basic
	@Column(name = "CONTENT_ENTITY")
	private String contentEntity;

	@Basic
	@Column(name = "CONTENT_ID")
	private Long contentId;

	@Basic
	@Column(name = "CONTENT")
	private String content;

}