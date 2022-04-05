package com.code;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.RepositoryManager;
import com.code.dal.entities.QueryNames;
import com.code.dal.entities.config.Configuration;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;

@Service
public class Test {

    @Autowired
    private RepositoryManager repositoryManager;

    public String getMessage() {
	return getConfiguration("Test").getConfigValue();
    }

    public List<Configuration> getConfigurations() {
	return searchConfigurations(null);
    }

    public Configuration getConfiguration(String code) {
	return BasicUtil.getFirstItem(searchConfigurations(code));
    }

    private List<Configuration> searchConfigurations(String code) {
	try {
	    return repositoryManager.getEntities(Configuration.class, QueryNames.SP_CONFIGURATION_GET_CONFIGURATIONS, QueryNames.SP_CONFIGURATION_GET_CONFIGURATIONS_PARAMS, BasicUtil.getValueOrEscape(code), BasicUtil.getEscapeString());
	} catch (RepositoryException e) {
	    e.printStackTrace();
	    return new ArrayList<Configuration>();
	}
    }
}
