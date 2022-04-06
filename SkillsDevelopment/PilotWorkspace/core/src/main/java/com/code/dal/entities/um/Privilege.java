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

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "UM_PRIVILEGES")
public class Privilege extends AuditeeEntity {
    @SequenceGenerator(name = "UMSeq", sequenceName = "UM_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "UMSeq", strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_GROUP_ID")
    private Long usersGroupId;

    @Column(name = "PRIVILEGE_GROUP_ID")
    private Long privilegeGroupId;

    @Override
    public String caculateContentId() {
	return id.toString();
    }
}
