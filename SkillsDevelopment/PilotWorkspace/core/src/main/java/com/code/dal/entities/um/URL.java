package com.code.dal.entities.um;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.code.dal.entities.base.BaseEntity;

import lombok.Data;

// TODO: Review queries
/*@NamedQueries({
	@NamedQuery(
		name = "um_menu_getUserMenus",
		query = " select m from Menu m, UserMenuData umd " +
			" where m.id = umd.menuId " +
			"   and umd.userId = :P_USER_ID " +
			"   and m.moduleId = :P_MODULE_ID " +
			"   and m.activeFlag = 1 " +
			" order by m.orderBy "),

	@NamedQuery(
		name = "um_menu_getMenus",
		query = " select m from Menu m " +
			" where m.moduleId = :P_MODULE_ID " +
			" order by m.id"),

	@NamedQuery(
		name = "um_menu_getLeafMenus",
		query = " select m from Menu m " +
			" where m.moduleId = :P_MODULE_ID " +
			" and m.url is not null" +
			" order by m.id "),

	@NamedQuery(
		name = "um_menu_getGroupMenusByGroupId",
		query = " select m from Menu m , GroupDetail gd " +
			" where m.id = gd.menuId " +
			" and gd.groupId  = :P_GROUP_ID " +
			" order by m.id "),

})
*/

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
