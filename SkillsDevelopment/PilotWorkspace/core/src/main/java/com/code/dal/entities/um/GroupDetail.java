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

//TODO: review queries

/*@NamedQueries({
	@NamedQuery(name = "um_groupDetail_getPrivilageAgenciesGroupDetails",
		query = " select distinct agencyGd from GroupDetail agencyGd,Privilege prv,GroupDetail userGd " +
			" where agencyGd.groupId = prv.agenciesGroupId " +
			" and prv.usersGroupId = userGd.groupId" +
			" and  (userGd.userId = :P_USER_ID) " +
			" order by agencyGd.id")
})*/

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UM_GROUP_DETAILS")
public class GroupDetail extends AuditeeEntity {
    @SequenceGenerator(name = "UMSeq", sequenceName = "UM_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "UMSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "GROUP_ID")
    private Long groupId;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "URL_ID")
    private Long urlId;

    @Column(name = "URL_ACTION_ID")
    private Long urlActionId;

    @Column(name = "DATA_DIMENSION")
    private Long dataDimension;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
