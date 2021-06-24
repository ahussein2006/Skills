package com.code.enums;

public enum FileTypesEnum {

    XLS("xls"),
    XLSX("xlsx");

    private String value;

    private FileTypesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
