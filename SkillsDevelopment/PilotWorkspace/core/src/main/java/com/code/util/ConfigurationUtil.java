package com.code.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.config.InjectionManager;
import com.code.dal.RepositoryManager;
import com.code.dal.entities.setup.Configuration;
import com.code.dal.entities.setup.Module;
import com.code.enums.ConfigCodesEnum;
import com.code.enums.QueryConfigConstants;
import com.code.exceptions.RepositoryException;

public class ConfigurationUtil {

    @Autowired
    private RepositoryManager repositoryManager;

    private static ConfigurationUtil instance;

    private static Map<String, String> configurationsMap;

    private static Long moduleId;

    static {
	instance = new ConfigurationUtil();
	InjectionManager.wireServices(BasicUtil.convertObjectToSet(instance));
	init();
    }

    private ConfigurationUtil() {
    }

    // ------------------------------------ Initialization -------------------------------------
    private static void init() {
	moduleId = getModuleIdByCode(ResourceBundleUtil.getModuleCode());
	configurationsMap = new HashMap<String, String>();
	List<Configuration> configurationsList = searchConfigurations();
	configurationsList.forEach(config -> configurationsMap.put(config.getCode(), config.getConfigValue()));
    }

    private static Long getModuleIdByCode(String moduleCode) {
	try {
	    return BasicUtil.getFirstItem(instance.repositoryManager.getEntities(Module.class, QueryConfigConstants.SP_Module_GetModules, QueryConfigConstants.SP_Module_GetModules_Params, moduleCode)).getId();
	} catch (RepositoryException e) {
	    ExceptionUtil.handleException(e, null);
	    return -1L;
	}
    }

    private static List<Configuration> searchConfigurations() {
	try {
	    return instance.repositoryManager.getEntities(Configuration.class, QueryConfigConstants.SP_Configuration_GetConfigurations, QueryConfigConstants.SP_Configuration_GetConfigurations_Params, moduleId);
	} catch (RepositoryException e) {
	    ExceptionUtil.handleException(e, null);
	    return new ArrayList<Configuration>();
	}
    }

    // -----------------------------------------------------------------------------------------
    public static Long getModuleId() {
	return moduleId;
    }

    public static String getConfigValue(ConfigCodesEnum configCodeEnum) {
	return configurationsMap.get(configCodeEnum.toString());
    }
}
