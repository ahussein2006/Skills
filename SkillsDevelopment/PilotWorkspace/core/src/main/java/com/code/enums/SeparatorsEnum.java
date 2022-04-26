package com.code.enums;

public enum SeparatorsEnum {

    COLON(":"),
    COMMA(","),
    DASH("-"),
    DOT("."),
    HASH("#"),
    PERCENT("%"),
    QUOTE("\"");

    private String value;

    private SeparatorsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
