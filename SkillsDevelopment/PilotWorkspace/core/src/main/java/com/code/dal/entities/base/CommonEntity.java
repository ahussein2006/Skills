package com.code.dal.entities.base;

import java.util.Date;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class CommonEntity implements BaseEntity {

    @JsonbTransient
    @Column(name = "INSERTION_DATE", updatable = false)
    private Date insertionDate;

    @JsonbTransient
    @Column(name = "LAST_UPDATE_DATE")
    private Date lastUpdateDate;

    @PrePersist
    private void prePersist() {
	this.insertionDate = new Date();
    }

    @PreUpdate
    private void preUpdate() {
	this.lastUpdateDate = new Date();
    }
}
