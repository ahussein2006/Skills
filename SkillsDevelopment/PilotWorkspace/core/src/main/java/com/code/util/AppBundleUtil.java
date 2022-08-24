package com.code.util;

import java.util.ResourceBundle;

import com.code.enums.AppBundleAttributesEnum;
import com.code.enums.BundlesEnum;

public class AppBundleUtil {

    private static ResourceBundle appProperties = ResourceBundle.getBundle(BundlesEnum.APPLICATION.getValue());

    private AppBundleUtil() {
    }

    public static String getModuleCode() {
	return getAppPropertyValue(AppBundleAttributesEnum.MODULE_CODE.getValue());
    }

    public static String getModuleMainPackage() {
	return getAppPropertyValue(AppBundleAttributesEnum.MODULE_MAIN_PACKAGE.getValue());
    }

    public static String getReportsRootParamName() {
	return getAppPropertyValue(AppBundleAttributesEnum.REPORTS_ROOT_PARAM_NAME.getValue());
    }

    public static String getReportsSchemaParamName() {
	return getAppPropertyValue(AppBundleAttributesEnum.REPORTS_SCHEMA_PARAM_NAME.getValue());
    }

    private static String getAppPropertyValue(String key) {
	return appProperties.getString(key);
    }
}
