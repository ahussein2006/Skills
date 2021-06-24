package com.code.enums;

public enum ErrorMessageCodesEnum {

    SHEET_UNSUPPORTED_FILE_FORMAT("error_sheetUnsupportedFileFormat"),
    SHEET_INDEX_OUT_OF_BOUND("error_sheetIndexOutOfBound"),
    SHEET_ROW_OUT_OF_BOUND("error_sheetRowOutOfBound"),
    SHEET_COLUMN_OUT_OF_BOUND("error_sheetColumnOutOfBound"),
    SHEET_UNSUPPORTED_CELL_TYPE("error_sheetUnsupportedCellType"),
    GENERAL("error_general");

    private String value;

    private ErrorMessageCodesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
