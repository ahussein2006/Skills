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

import com.code.dal.entities.base.AuditeeEntity;
import com.code.enums.QueryConfigConstants;

import lombok.Data;
import lombok.EqualsAndHashCode;

@NamedQueries({
	@NamedQuery(
		name = QueryConfigConstants.UM_User_GetUsersByURL,
		query = " select u from User u, UserURLData ud " +
			" where ud.userId = u.id " +
			"   and ud.urlId = :P_URL_ID " +
			" order by u.name "),

	@NamedQuery(
		name = QueryConfigConstants.UM_User_GetUsersByAction,
		query = " select u from User u, UserURLActionData ua " +
			" where ua.userId = u.id " +
			"   and ua.id = :P_URL_ACTION_ID " +
			" order by u.name ")
})

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UM_USERS")
public class User extends AuditeeEntity {
    @SequenceGenerator(name = "UMSeq", sequenceName = "UM_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "UMSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "SYSTEM_FLAG")
    private Integer systemFlag;

    @Column(name = "TEMP_KEY")
    private String tempKey;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
