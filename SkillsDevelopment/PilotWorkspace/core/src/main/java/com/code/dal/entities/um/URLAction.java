package com.code.dal.entities.um;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.code.dal.entities.base.BaseEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.UM_URLAction_GetGroupURLActions,
		query = " select a from URLAction a, GroupDetail gd " +
			" where gd.urlActionId = a.id " +
			"   and gd.groupId  = :P_GROUP_ID " +
			" order by a.urlId, a.description ")
})

@Data
@Entity
@Table(name = "UM_URL_ACTIONS")
public class URLAction implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "URL_ID")
    private Long urlId;

    @Column(name = "DESCRIPTION")
    private String description;
}
