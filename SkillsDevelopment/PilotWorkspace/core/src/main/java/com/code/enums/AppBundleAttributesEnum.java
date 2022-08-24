package com.code.enums;

public enum AppBundleAttributesEnum {
    MODULE_CODE("module.code"),
    MODULE_MAIN_PACKAGE("module.main.package"),

    REPORTS_ROOT_PARAM_NAME("reports.root.param.name"),
    REPORTS_SCHEMA_PARAM_NAME("reports.schema.param.name");

    private String value;

    private AppBundleAttributesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
