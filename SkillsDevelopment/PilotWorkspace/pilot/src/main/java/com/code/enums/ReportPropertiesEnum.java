package com.code.enums;

public enum ReportPropertiesEnum {
    MSN_MISSION_DETAILS("/Pilot/Missions/MSN_MissionDetails.jrxml#P_MISSION_ID,P_PRINT_DATE");

    private String value;

    private ReportPropertiesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
