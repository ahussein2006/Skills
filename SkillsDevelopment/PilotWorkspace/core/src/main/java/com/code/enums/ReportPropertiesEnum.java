package com.code.enums;

public enum ReportPropertiesEnum {
    REPORTS_ROOT("P_REPORTS_ROOT"),
    SCHEMA_NAME("P_SCHEMA_NAME"),

    CONFIG_CONFIGURATIONS("/Pilot/Config_Configurations.jrxml#P_CONFIG_CODE,P_PRINT_DATE");

    private String value;

    private ReportPropertiesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }
}
