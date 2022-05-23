package com.code.enums;

public enum WFTaskActionsEnum {

    REJECT("Reject"),
    RETURN_TO_REVIEWER("Return"),
    SIGN("Sign"),
    REVIEW("Review"),
    NOTIFIED("Notified");

    private String value;

    private WFTaskActionsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
