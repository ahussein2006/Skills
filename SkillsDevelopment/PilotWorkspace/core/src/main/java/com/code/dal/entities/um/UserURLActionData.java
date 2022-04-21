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
		name = QueryConfigConstants.UM_UserURLActionData_GetUserURLActionsData,
		query = " select ua from UserURLActionData ua, URL u " +
			" where ua.urlId = u.id " +
			"   and ua.userId = :P_USER_ID " +
			"   and u.moduleId = :P_MODULE_ID " +
			"   and u.activeFlag = 1 " +
			" order by ua.urlId, ua.id ")
})

@Data
@Entity
@Table(name = "UM_VW_USER_URL_ACTIONS")
public class UserURLActionData implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "URL_ID")
    private Long urlId;

    @Column(name = "URL_CODE")
    private String urlCode;

    @Column(name = "ACTION")
    private String action;
}
