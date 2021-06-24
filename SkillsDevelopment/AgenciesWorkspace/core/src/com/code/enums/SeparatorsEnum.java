package com.code.enums;

public enum SeparatorsEnum {

    DASH(" - "),
    COMMA(", ");

    private String value;

    private SeparatorsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
