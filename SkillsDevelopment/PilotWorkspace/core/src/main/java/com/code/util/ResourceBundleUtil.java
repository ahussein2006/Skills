package com.code.util;

import java.util.ResourceBundle;

import com.code.enums.BundlesEnum;

public class ResourceBundleUtil {

    private static ResourceBundle appProperties = ResourceBundle.getBundle(BundlesEnum.APPLICATION.getValue());

    private ResourceBundleUtil() {
    }

    public static String getAppPropertyValue(String key) {
	return appProperties.getString(key);
    }
}
