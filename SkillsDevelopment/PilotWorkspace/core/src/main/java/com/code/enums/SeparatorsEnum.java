package com.code.enums;

public enum SeparatorsEnum {

    CLOSING_SET_BRACKET("}"),
    COLON(":"),
    COMMA(","),
    DASH("-"),
    DOT("."),
    HASH("#"),
    PERCENT("%"),
    QUOTE("\""),
    SLASH("/"),
    SPACE(" "),
    UNDERSCORE("_");

    private String value;

    private SeparatorsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
