package com.code.dal.entities;

import java.io.Serializable;

import javax.persistence.Transient;

import com.code.business.util.BasicUtil;

public abstract class BaseEntity implements Serializable {
    private boolean selected;

    @Transient
    public boolean getSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public abstract void setGlobalId();

    protected String getGlobalId() {
	return BasicUtil.generateUUID();
    }
}