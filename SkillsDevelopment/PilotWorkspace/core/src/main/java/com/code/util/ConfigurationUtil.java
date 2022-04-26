package com.code.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.config.InjectionManager;
import com.code.dal.RepositoryManager;
import com.code.dal.entities.config.Configuration;
import com.code.enums.ConfigCodesEnum;
import com.code.enums.QueryConfigConstants;
import com.code.exceptions.RepositoryException;

public class ConfigurationUtil {

    @Autowired
    private RepositoryManager repositoryManager;

    private static ConfigurationUtil instance;

    private static Map<String, String> configurationsMap;

    static {
	instance = new ConfigurationUtil();
	InjectionManager.wireServices(BasicUtil.convertObjectToSet(instance));
	init();
    }

    private ConfigurationUtil() {
    }

    // ----------------------------------------------------------------------------------------

    private static void init() {
	List<Configuration> configurationsList;
	configurationsMap = new HashMap<String, String>();
	configurationsList = searchConfigurations(null);
	configurationsList.forEach(config -> configurationsMap.put(config.getCode(), config.getConfigValue()));
    }

    private static List<Configuration> searchConfigurations(String code) {
	try {
	    return instance.repositoryManager.getEntities(Configuration.class, QueryConfigConstants.SP_Configuration_GetConfigurations, QueryConfigConstants.SP_Configuration_GetConfigurations_Params, BasicUtil.getValueOrEscape(code));
	} catch (RepositoryException e) {
	    LoggingUtil.logException(e, ConfigurationUtil.class.getCanonicalName());
	    return new ArrayList<Configuration>();
	}
    }

    // ----------------------------------------------------------------------------------------

    public static Long getModuleId() {
	return Long.parseLong(configurationsMap.get(ResourceBundleUtil.getModuleCode()));
    }

    public static String getConfigValue(ConfigCodesEnum configCodeEnum) {
	return configurationsMap.get(configCodeEnum.toString());
    }
}
