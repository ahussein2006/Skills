package com.code.util;

import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
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

    public static <T> T convertFromJson(Class<T> objectClass, String jsonString) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.fromJson(jsonString, objectClass);
    }

    public static <T> List<T> convertFromJsonArray(Class<T> objectClass, String jsonString) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.fromJson(jsonString, new ArrayList<T>() {
	}.getClass().getGenericSuperclass());
    }

    // -----------------------------------------------------------------------------------------
    public static String convertToJsonString(String[] fields, Object[] values) {
	return convertToJsonObject(fields, values).toString();
    }

    public static JsonObject convertToJsonObject(String[] fields, Object[] values) {
	JsonObjectBuilder builder = Json.createObjectBuilder();
	for (int i = 0; i < fields.length; i++) {
	    if (values[i] == null)
		builder.add(fields[i], "");
	    else if (values[i] instanceof JsonObjectBuilder)
		builder.add(fields[i], (JsonObjectBuilder) values[i]);
	    else if (values[i] instanceof JsonObject)
		builder.add(fields[i], (JsonObject) values[i]);
	    else if (values[i] instanceof JsonArrayBuilder)
		builder.add(fields[i], (JsonArrayBuilder) values[i]);
	    else if (values[i] instanceof Integer)
		builder.add(fields[i], (Integer) values[i]);
	    else if (values[i] instanceof Long)
		builder.add(fields[i], (Long) values[i]);
	    else if (values[i] instanceof Double)
		builder.add(fields[i], (Double) values[i]);
	    else
		builder.add(fields[i], values[i].toString());
	}
	return builder.build();
    }

}
