package com.code.dal.entities.audit;

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

import com.code.dal.entities.base.BaseEntity;

import lombok.Data;

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

    @Column(name = "SYSTEM_NAME")
    private String systemName;

    @Column(name = "OPERATION")
    private String operation;

    @Column(name = "OPERATION_GREG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationGregDate;

    @Column(name = "CONTENT_ENTITY")
    private String contentEntity;

    @Column(name = "CONTENT_ID")
    private String contentId;

    @Column(name = "CONTENT")
    private String content;
}