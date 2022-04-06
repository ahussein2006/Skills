package com.code.dal.entities.um;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.entities.base.AuditeeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/*@NamedQueries({

	@NamedQuery(name = "um_group_getGroups",
		query = "select g from Group g " +
			" where g.moduleId = :P_MODULE_ID " +
			" and g.groupType = :P_GROUP_TYPE " +
			" and (:P_GROUP_NAME = :P_ESC_SEARCH_STR or g.name like :P_GROUP_NAME) " +
			" and (:P_USER_AGENCY_ID = :P_ESC_SEARCH_STR or g.userAgencyId = :P_USER_AGENCY_ID ) " +
			" and (:P_USER_NAME = :P_ESC_SEARCH_STR or (select count(d.id) from GroupDetailData d where d.groupId = g.id and d.userName like :P_USER_NAME ) > 0) " +
			" and (:P_MENU_ID = :P_ESC_SEARCH_LONG or (select count(d.id) from GroupDetail d where d.groupId = g.id and d.menuId = :P_MENU_ID) > 0) " +
			" and (:P_MENU_ACTION_ID = :P_ESC_SEARCH_LONG or (select count(d.id) from GroupDetail d where d.groupId = g.id and d.menuActionId = :P_MENU_ACTION_ID ) > 0) " +
			" and (:P_AGENCY_NAME = :P_ESC_SEARCH_STR or (select count(d.id) from GroupDetailData d where d.groupId = g.id and d.agencyName like :P_AGENCY_NAME ) > 0) " +
			" order by g.id")
})
*/

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UM_GROUPS")
public class Group extends AuditeeEntity {
    @SequenceGenerator(name = "UMSeq", sequenceName = "UM_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "UMSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "MODULE_ID")
    private Long moduleId;

    @Column(name = "CLASSIFICATION")
    private Integer classification;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
