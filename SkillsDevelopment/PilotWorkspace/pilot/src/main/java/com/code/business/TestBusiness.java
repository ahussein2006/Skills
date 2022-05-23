package com.code.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.dal.ReportManager;
import com.code.dal.RepositoryManager;
import com.code.dal.entities.config.Configuration;
import com.code.enums.ReportOutputFormatsEnum;
import com.code.enums.ReportPropertiesEnum;
import com.code.exceptions.RepositoryException;
import com.code.util.BasicUtil;

@Service
public class TestBusiness {

    @Autowired
    private RepositoryManager repositoryManager;

    @Autowired
    private ReportManager reportManager;

    // ------------------------------------ Test Configuration ---------------------------------
    public Configuration getConfigByCode(String code) {
	try {
	    return BasicUtil.getFirstItem(repositoryManager.getEntities(Configuration.class, "SP_Configuration_GetConfigurations", "P_CODE", code));
	} catch (RepositoryException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public void addConfig(Configuration config) {
	try {
	    repositoryManager.insertEntity(config, 1);
	} catch (RepositoryException e) {
	    e.printStackTrace();
	}
    }

    // ------------------------------------ Test Reports ---------------------------------------
    public byte[] getReportData(ReportOutputFormatsEnum reportOutputFormat) {
	try {
	    return reportManager.executeReport(ReportPropertiesEnum.CONFIG_CONFIGURATIONS, reportOutputFormat, "REPORTS_ROOT", "26/04/2022");
	} catch (RepositoryException e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
