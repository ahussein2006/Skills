package com.code.enums;

public enum BundlesEnum {
    APPLICATION("application"),
    MESSAGES("messages"),

    MODULE_CODE("module.code"),
    MODULE_MAIN_PACKAGE("module.main.package");

    private String value;

    private BundlesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
