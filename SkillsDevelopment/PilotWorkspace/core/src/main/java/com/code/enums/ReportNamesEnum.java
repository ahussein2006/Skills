package com.code.enums;

public enum ReportNamesEnum {

    CONFIG_CONFIGURATIONS("/Pilot/Config_Configurations.jrxml");

    private String code;

    private ReportNamesEnum(String code) {
	this.code = code;
    }

    public String getCode() {
	return code;
    }
}
