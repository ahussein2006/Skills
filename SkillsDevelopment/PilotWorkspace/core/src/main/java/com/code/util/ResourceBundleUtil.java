package com.code.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.config.InjectionManager;
import com.code.dal.RepositoryManager;
import com.code.dal.entities.setup.Message;
import com.code.enums.BundlesEnum;
import com.code.enums.FileTypesEnum;
import com.code.enums.LocalesEnum;
import com.code.enums.QueryConfigConstants;
import com.code.enums.SeparatorsEnum;
import com.code.exceptions.RepositoryException;

public class ResourceBundleUtil {

    private static ResourceBundle appProperties = ResourceBundle.getBundle(BundlesEnum.APPLICATION.getValue());
    private static ResourceBundle arabicMessages;
    private static ResourceBundle englishMessages;

    @Autowired
    private RepositoryManager repositoryManager;

    private static ResourceBundleUtil instance;

    private static String rootPath;

    static {
	instance = new ResourceBundleUtil();
	InjectionManager.wireServices(BasicUtil.convertObjectToSet(instance));
	init();
    }

    private ResourceBundleUtil() {
	rootPath = this.getClass().getClassLoader().getResource("").getPath();
    }

    // -----------------------------------------------------------------------------------------

    private static void init() {
	List<Message> messages = searchMessages(BasicUtil.getValueLikeOrEscape(SeparatorsEnum.COMMA.getValue() + ConfigurationUtil.getModuleId() + SeparatorsEnum.COMMA.getValue()));

	Properties arabicProperties = new Properties();
	Properties englishProperties = new Properties();

	String arabicPropertiesFilePath = rootPath + BundlesEnum.MESSAGES.getValue() + SeparatorsEnum.UNDERSCORE.getValue()
		+ LocalesEnum.AR.getValue() + SeparatorsEnum.DOT.getValue() + FileTypesEnum.PROPERTIES.getValue();
	String englishPropertiesFilePath = rootPath + BundlesEnum.MESSAGES.getValue() + SeparatorsEnum.UNDERSCORE.getValue()
		+ LocalesEnum.EN.getValue() + SeparatorsEnum.DOT.getValue() + FileTypesEnum.PROPERTIES.getValue();

	try (OutputStream arabicOutputStream = new BufferedOutputStream(new FileOutputStream(arabicPropertiesFilePath));
		OutputStream englishOutputStream = new BufferedOutputStream(new FileOutputStream(englishPropertiesFilePath))) {

	    messages.forEach(message -> {
		arabicProperties.setProperty(message.getMessageKey(), message.getArValue());
		englishProperties.setProperty(message.getMessageKey(), message.getEnValue());
	    });
	    arabicProperties.store(arabicOutputStream, null);
	    englishProperties.store(englishOutputStream, null);

	    arabicMessages = ResourceBundle.getBundle(BundlesEnum.MESSAGES.getValue(), new Locale(LocalesEnum.AR.getValue()));
	    englishMessages = ResourceBundle.getBundle(BundlesEnum.MESSAGES.getValue(), new Locale(LocalesEnum.EN.getValue()));
	} catch (Exception e) {
	    ExceptionUtil.handleException(e, null);
	}
    }

    private static List<Message> searchMessages(String moduleIds) {
	try {
	    return instance.repositoryManager.getEntities(Message.class, QueryConfigConstants.SP_Message_GetMessages, QueryConfigConstants.SP_Message_GetMessages_Params, moduleIds);
	} catch (RepositoryException e) {
	    ExceptionUtil.handleException(e, null);
	    return new ArrayList<Message>();
	}
    }

    // -----------------------------------------------------------------------------------------

    public static String getModuleCode() {
	return getAppPropertyValue(BundlesEnum.MODULE_CODE.getValue());
    }

    public static String getModuleMainPackage() {
	return getAppPropertyValue(BundlesEnum.MODULE_MAIN_PACKAGE.getValue());
    }

    public static String getAppPropertyValue(String key) {
	return appProperties.getString(key);
    }

    public static List<Message> getMessages(String moduleIds) {
	return searchMessages(moduleIds);
    }

    public static String getMessage(String key, LocalesEnum locale, Object... params) {
	try {
	    String value = locale.equals(LocalesEnum.AR) ? arabicMessages.getString(key) : englishMessages.getString(key);
	    return params == null ? value : MessageFormat.format(value, params);
	} catch (MissingResourceException e) {
	    return key;
	}
    }
}
