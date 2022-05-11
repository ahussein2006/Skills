package com.code.enums;

public enum PatternsEnum {

    DIGITS_ONLY("\\d+"),
    TWO_DIGITS("00"),
    FULL_TIMESTAMP("yyyy/MM/dd HH:mm:ss");

    private String value;

    private PatternsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
