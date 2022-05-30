package com.code.enums;

public enum WFTaskRolesEnum {
    REQUESTER("Requester"),

    DIRECT_MANAGER("DirectManager"),

    MANAGER_REDIRECT("ManagerRedirect"),

    REVIEWER_EMP("ReviewerEmp"),

    SIGN_MANAGER("SignManager"),

    NOTIFICATION("Notification"),
    HISTORY("History");

    private String value;

    private WFTaskRolesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
