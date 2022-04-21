package com.code.dal.entities.um;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.entities.base.BaseEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.UM_URL_GetUserURLs,
		query = " select u from URL u, UserURLData urd " +
			" where urd.urlId = u.id " +
			"   and urd.userId = :P_USER_ID " +
			"   and u.moduleId = :P_MODULE_ID " +
			"   and u.activeFlag = 1 " +
			" order by u.orderBy "),

	@NamedQuery(
		name = QueryConfigConstants.UM_URL_GetGroupURLs,
		query = " select u from URL u , GroupDetail gd " +
			" where gd.urlId = u.id " +
			"   and gd.groupId  = :P_GROUP_ID " +
			" order by u.orderBy "),
})

@Data
@Entity
@Table(name = "UM_URLS")
public class URL implements BaseEntity {
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "URL")
    private String url;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "NAME_KEY")
    private String nameKey;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "ORDER_BY")
    private Integer orderBy;

    @Column(name = "CLASSIFICATION")
    private Integer classification;

    @Column(name = "ACTIVE_FLAG")
    private Integer activeFlag;

    @Transient
    private List<URL> subMenus;
}
