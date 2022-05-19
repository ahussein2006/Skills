package com.code.enums;

public enum WFTaskActionsEnum {

    REJECT("\u0631\u0641\u0640\u0640\u0636"),
    RETURN_TO_REVIEWER("\u0627\u0631\u062C\u0627\u0639 \u0627\u0644\u0645\u062F\u0642\u0642"),
    SIGN("\u0625\u0639\u062A\u0645\u0627\u062F"),
    REVIEW("\u062A\u062F\u0642\u064A\u0642"),
    NOTIFIED("\u062A\u0640\u0640\u0640\u0645 \u0627\u0644\u0625\u0637\u0640\u0640\u0640\u0644\u0627\u0639");

    private String value;

    private WFTaskActionsEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
