package com.code.business.util;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class ContentUtil {

    private ContentUtil() {

    }

    public static String convertToJsonString(Object object) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.toJson(object);
    }

    public static <T> T convertFromJson(Class<T> objectClass, String jsonString) {
	Jsonb jsonb = JsonbBuilder.create();
	return jsonb.fromJson(jsonString, objectClass);
    }

}
