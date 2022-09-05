package com.code.util;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.config.InjectionManager;
import com.code.dal.RepositoryManager;
import com.code.dal.entities.general.Log;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.ConfigCodesEnum;
import com.code.enums.LogTypesEnum;
import com.code.enums.LoggingTechniquesEnum;
import com.code.enums.SeparatorsEnum;

public class LoggingUtil {

    @Autowired
    private RepositoryManager repositoryManager;

    private static LoggingUtil instance;

    static {
	instance = new LoggingUtil();
	InjectionManager.wireServices(BasicUtil.convertObjectToSet(instance));
    }

    private LoggingUtil() {
    }

    public static void log(String message, Long userId, LogTypesEnum... logType) {
	log(new Log(null, ConfigurationUtil.getModuleId(), BasicUtil.isNullOrEmpty(logType) ? LogTypesEnum.LOG_ERROR.toString() : logType[0].toString(), MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN), message, userId, null, null));
    }

    public static void log(String message, Long userId, String serviceRequestUrl, String serviceRequestId, LogTypesEnum logType) {
	log(new Log(null, ConfigurationUtil.getModuleId(), logType.toString(), MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.GREGORIAN), message, userId, serviceRequestUrl, serviceRequestId));
    }

    private static void log(Log log) {
	LoggingTechniquesEnum loogingTechnique = LoggingTechniquesEnum.valueOf(ConfigurationUtil.getConfigValue(ConfigCodesEnum.valueOf(log.getLogType())));

	if (loogingTechnique.equals(LoggingTechniquesEnum.ALL) || loogingTechnique.equals(LoggingTechniquesEnum.CONSOLE))
	    logToConsole(log);

	if (loogingTechnique.equals(LoggingTechniquesEnum.ALL) || loogingTechnique.equals(LoggingTechniquesEnum.REPOSITORY))
	    logToRepository(log);
    }

    private static void logToConsole(Log log) {
	System.out.println("----------------------------------------------------------------------------------------------------------");

	System.out.println(" <<< " + log.getLogType() + SeparatorsEnum.COLON.getValue() + MultiChronologyCalendarUtil.getTimestampString(log.getLogDate(), ChronologyTypesEnum.GREGORIAN) + " >>> ");

	if (log.getUserId() != null)
	    System.out.println("UserId: " + log.getUserId());

	if (log.getServiceRequestUrl() != null)
	    System.out.println("ServiceRequestUrl: " + log.getServiceRequestUrl());

	if (log.getServiceRequestId() != null)
	    System.out.println("ServiceRequestId: " + log.getServiceRequestId());

	System.out.println(log.getLogData());

	System.out.println("----------------------------------------------------------------------------------------------------------");
    }

    private static void logToRepository(Log log) {
	try {
	    instance.repositoryManager.insertEntity(log, -1);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
