package com.code.dal.entities.um;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.code.dal.entities.base.BaseEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "UM_VW_USER_URLS")
public class UserURLData implements BaseEntity {
    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Id
    @Column(name = "URL_ID")
    private Long urlId;
}
