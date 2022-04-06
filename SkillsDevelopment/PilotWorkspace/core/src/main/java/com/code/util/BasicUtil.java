package com.code.util;

import java.lang.StackWalker.StackFrame;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.code.enums.FlagsEnum;
import com.code.enums.SeparatorsEnum;

public class BasicUtil {

    private BasicUtil() {
    }

    // ----------------------------------- UUID -----------------------------------------------
    public static String generateUUID() {
	return UUID.randomUUID().toString();
    }

    // -------------------------------- Stack Frames ------------------------------------------
    public static String getCallerMethod() {
	StackFrame sf = StackWalker.getInstance().walk(stackFrames -> stackFrames
		.filter(stackFrame -> stackFrame.getClassName().contains("com.code."))
		.skip(2)
		.findFirst()).get();
	return sf.getClassName() + "." + sf.getMethodName();
    }

    // ------------------------------ Format Operations ---------------------------------------
    public static boolean isDigit(String input) {
	return input.matches("\\d+");
    }

    // --------------------------------- Separation -------------------------------------------
    public static String constructSeparatedValues(String separator, String... values) {
	if (isNullOrEmpty(values))
	    return null;

	StringBuilder sb = new StringBuilder(values[0]);
	for (int i = 1; i < values.length; i++)
	    sb.append(separator).append(values[i]);

	return sb.toString();
    }

    public static String[] getSeparatedValues(String separator, String values) {
	if (isNullOrEmpty(values))
	    return null;

	return values.split(separator);
    }

    // -------------------------------- Building Maps -----------------------------------------
    public static Map<String, Object> getParams(String paramNames, Object... paramValues) {
	Map<String, Object> params = new HashMap<String, Object>();
	if (hasValue(paramNames) && hasElements(paramValues)) {
	    String[] paramNamesArray = getSeparatedValues(SeparatorsEnum.COMMA.getValue(), paramNames);
	    for (int i = 0; i < paramNamesArray.length; i++)
		params.put(paramNamesArray[i], paramValues[i]);
	}
	return params;
    }

    // --------------------- Null-Empty-Value-Elements Operations -----------------------------
    public static boolean isNullOrEmpty(Object[] array) {
	return (array == null || array.length == 0);
    }

    public static <T> boolean isNullOrEmpty(List<T> list) {
	return (list == null || list.isEmpty());
    }

    public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
	return (map == null || map.isEmpty());
    }

    public static boolean isNullOrEmpty(String value) {
	return (value == null || value.trim().isEmpty());
    }

    public static boolean hasElements(Object[] array) {
	return (array != null && array.length != 0);
    }

    public static <T> boolean hasElements(List<T> list) {
	return (list != null && !list.isEmpty());
    }

    public static <K, V> boolean hasElements(Map<K, V> map) {
	return (map != null && !map.isEmpty());
    }

    public static boolean hasValue(String value) {
	return (value != null && !value.trim().isEmpty());
    }

    public static <T> T getFirstItem(List<T> list) {
	return isNullOrEmpty(list) ? null : list.get(0);
    }

    // ------------------------------- Escaped Values -----------------------------------------
    public static String getValueOrEscape(String value) {
	return isNullOrEmpty(value) ? FlagsEnum.ALL.getCode() + "" : value;
    }

    public static String getValueLikeOrEscape(String value) {
	return isNullOrEmpty(value) ? FlagsEnum.ALL.getCode() + "" : "%" + value + "%";
    }

    public static int getValueOrEscape(Integer value) {
	return value == null ? FlagsEnum.ALL.getCode() : value;
    }

    public static long getValueOrEscape(Long value) {
	return value == null ? FlagsEnum.ALL.getCode() : value;
    }

    public static double getValueOrEscape(Double value) {
	return value == null ? FlagsEnum.ALL.getCode() : value;
    }

    public static String getEscapeString() {
	return FlagsEnum.ALL.getCode() + "";
    }

    public static int getEscapeInteger() {
	return FlagsEnum.ALL.getCode();
    }

    public static long getEscapeLong() {
	return FlagsEnum.ALL.getCode();
    }

    public static double getEscapeDouble() {
	return FlagsEnum.ALL.getCode();
    }

}
