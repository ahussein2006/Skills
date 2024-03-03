package com.code.enums;

public enum JsonAttributesEnum {
    HIJRI_CALENDAR("hijriCalendar"),
    GREGORIAN_CALENDAR("gregorianCalendar"),
    HIJRI_YEARS("hijriYears"),
    START_VALID_HIJRI_YEAR("startValidHijriYear"),
    END_VALID_HIJRI_YEAR("endValidHijriYear"),
    GREGORIAN_YEARS("gregorianYears"),
    START_VALID_GREGORIAN_YEAR("startValidGregYear"),
    END_VALID_GREGORIAN_YEAR("endValidGregYear"),

    PROVIDER_REQUEST_ID("providerRequestId");

    private String value;

    private JsonAttributesEnum(String value) {
	this.value = value;
    }

    public String getValue() {
	return this.value;
    }
}
