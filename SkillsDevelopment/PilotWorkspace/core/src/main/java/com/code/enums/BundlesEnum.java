package com.code.enums;

public enum BundlesEnum {
    APPLICATION("application"),
    MESSAGES("messages");

    private String value;

    private BundlesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
