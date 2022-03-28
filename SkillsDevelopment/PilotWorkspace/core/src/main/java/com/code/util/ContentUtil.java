package com.code.util;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class ContentUtil {

    private ContentUtil() {
    }

    public static String convertToJsonString(Object object) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.toJson(object);
    }

    public static String convertToJsonArrayString(List<Object> object) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.toJson(object);
    }

    public static String convertToJsonString(String[] fields, Object[] values) {
	JsonObjectBuilder builder = Json.createObjectBuilder();
	for (int i = 0; i < fields.length; i++) {
	    builder.add(fields[i], values[i] == null ? "" : values[i].toString());
	}
	return builder.build().toString();
    }

    public static <T> T convertFromJson(Class<T> objectClass, String jsonString) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.fromJson(jsonString, objectClass);
    }

    public static <T> List<T> convertFromJsonArray(Class<T> objectClass, String jsonString) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.fromJson(jsonString, new ArrayList<T>() {
	}.getClass().getGenericSuperclass());
    }

}
