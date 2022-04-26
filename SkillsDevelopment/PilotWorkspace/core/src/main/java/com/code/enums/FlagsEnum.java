package com.code.enums;

public enum FlagsEnum {

    ON(1),
    OFF(0),
    ALL(-1);

    private int value;

    private FlagsEnum(int value) {
	this.value = value;
    }

    public int getValue() {
	return value;
    }
}
