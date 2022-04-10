package com.code.dal.entities.um;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.code.dal.entities.QueryConfiguration;
import com.code.dal.entities.base.AuditeeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfiguration.UM_GROUP_GET_GROUPS,
		query = "select g from Group g " +
			" where g.moduleId = :P_MODULE_ID " +
			"   and g.classification = :P_CLASSIFICATION " +
			"   and (:P_NAME = :P_ESC_SEARCH_STR or g.name like :P_NAME) " +
			"   and (:P_USER_ID = :P_ESC_SEARCH_LONG or (select count(d.id) from GroupDetail d where d.groupId = g.id and d.userId = :P_USER_ID ) > 0) " +
			"   and (:P_URL_ID = :P_ESC_SEARCH_LONG or (select count(d.id) from GroupDetail d where d.groupId = g.id and d.urlId = :P_URL_ID) > 0) " +
			"   and (:P_URL_ACTION_ID = :P_ESC_SEARCH_LONG or (select count(d.id) from GroupDetail d where d.groupId = g.id and d.urlActionId = :P_URL_ACTION_ID ) > 0) " +
			" order by g.id")
})

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
