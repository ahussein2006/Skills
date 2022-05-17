package com.code.enums;

public enum PatternsEnum {

    DIGITS_ONLY("\\d+"),
    TWO_DIGITS("00"),

    SHORT_TIME("HH:mm"),
    LONG_TIME("HH:mm:ss"),
    HIJRI_DATE("mm/MM/yyyy"),
    GREG_DATE("dd/MM/yyyy");

    private String value;

    private PatternsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
