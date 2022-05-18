package com.code.dal.entities.workflow;

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
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.QueryConfigConstants;
import com.code.util.MultiChronologyCalendarUtil;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.WF_Instance_GetInstanceById,
		query = " select i from WFInstance i " +
			" where i.id = :P_ID ")
})

@Data
@Entity
@Table(name = "WF_INSTANCES")
public class WFInstance implements BaseEntity {
    @SequenceGenerator(name = "WFInstanceTaskSeq", sequenceName = "WF_INSTANCE_TASK_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "WFInstanceTaskSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "PROCESS_ID")
    private Long processId;

    @Column(name = "REQUESTER_ID")
    private Long requesterId;

    @Column(name = "SUBJECT")
    private String subject;

    @JsonbTransient
    @Column(name = "REQUEST_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    @JsonbTransient
    @Column(name = "REQUEST_HIJRI_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date requestHijriDate;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "LAST_ACTION")
    private String lastAction;

    @Column(name = "ATTACHMENTS_KEY")
    private String attachmentsKey;

    public String getRequestDateString() {
	return MultiChronologyCalendarUtil.getDateString(requestDate, ChronologyTypesEnum.GREGORIAN);
    }

    public String getRequestHijriDateString() {
	return MultiChronologyCalendarUtil.getDateString(requestHijriDate, ChronologyTypesEnum.HIJRI);
    }
}