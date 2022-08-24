package com.code.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.code.enums.ErrorMessageCodesEnum;
import com.code.exceptions.BusinessException;

public class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static <T> BusinessException handleException(Exception e, Long userId, String... additionalInfo) {
	if (e instanceof BusinessException)
	    return (BusinessException) e;

	String loggingMessage = getStackTrace(e);

	if (BasicUtil.hasElements(additionalInfo))
	    for (String info : additionalInfo)
		loggingMessage += System.lineSeparator() + info;

	LoggingUtil.log(loggingMessage, userId);

	return new BusinessException(ErrorMessageCodesEnum.GENERAL);
    }

    private static String getStackTrace(Exception e) {
	StringWriter stringWriter = new StringWriter();
	e.printStackTrace(new PrintWriter(stringWriter));
	return stringWriter.toString();
    }
}
