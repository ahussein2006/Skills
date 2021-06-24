package com.code.business.util;

import java.util.UUID;

public class BasicUtil {

    private BasicUtil() {
    }

    public static String generateUUID() {
	return UUID.randomUUID().toString();
    }

    public static String constructSeparatedValues(String separator, String... values) {
	if (isNullOrEmpty(values))
	    return null;

	StringBuilder sb = new StringBuilder(values[0]);
	for (int i = 1; i < values.length; i++)
	    sb.append(separator).append(values[i]);

	return sb.toString();
    }

    public static boolean isNullOrEmpty(Object[] array) {
	return (array == null || array.length == 0);
    }

    public static boolean hasElements(Object[] array) {
	return (array != null && array.length != 0);
    }

}
