package com.code.enums;

public enum LocalesEnum {
    AR("ar"),
    EN("en");

    private String value;

    private LocalesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
