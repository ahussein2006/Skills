package com.code.enums;

public enum WFTaskActionsEnum {

    APPROVE("Approve"),
    REJECT("Reject"),
    REDIRECT("Redirect"),
    REVIEW("Review"),
    SIGN("Sign"),
    RETURN_TO_REVIEWER("Return"),
    NOTIFIED("Notified");

    private String value;

    private WFTaskActionsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
