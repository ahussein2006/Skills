package com.code.enums;

public enum WFInstanceStatusesEnum {
    RUNNING(1),
    DONE(2),
    COMPLETED(3);

    private int value;

    private WFInstanceStatusesEnum(int value) {
	this.value = value;
    }

    public int getValue() {
	return value;
    }
}