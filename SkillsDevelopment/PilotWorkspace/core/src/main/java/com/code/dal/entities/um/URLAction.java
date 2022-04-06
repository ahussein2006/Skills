package com.code.dal.entities.um;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.code.dal.entities.base.BaseEntity;

import lombok.Data;

// TODO: Review queries

/*@NamedQueries({

	@NamedQuery(name = "um_menuAction_getMenusActions",
		query = " select ma from MenuAction ma, Menu m " +
			" where ma.menuId = m.id " +
			" and m.moduleId = :P_MODULE_ID " +
			" order by ma.id "),

	@NamedQuery(name = "um_menuAction_getMenusActionsByGroupId",
		query = " select ma from MenuAction ma, GroupDetail gd " +
			" where ma.id = gd.menuActionId " +
			" and gd.groupId  = :P_GROUP_ID " +
			" order by ma.id ")
})
*/

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
