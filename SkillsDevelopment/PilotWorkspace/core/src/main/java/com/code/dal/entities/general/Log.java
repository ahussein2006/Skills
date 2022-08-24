package com.code.dal.entities.general;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.entities.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GN_LOGS")
public class Log implements BaseEntity {

    @SequenceGenerator(name = "LogSeq", sequenceName = "GN_LOG_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "LogSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "LOG_TYPE")
    private String logType;

    @Column(name = "LOG_DATE")
    private Date logDate;

    @Lob
    @Column(name = "LOG_DATA")
    private String logData;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "INTEG_REQUEST_ID")
    private String integRequestId;

}
