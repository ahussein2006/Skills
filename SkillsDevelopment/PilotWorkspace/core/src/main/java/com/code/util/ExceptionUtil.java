package com.code.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.code.enums.ErrorMessageCodesEnum;
import com.code.enums.ServiceResponseStatusesCode;
import com.code.exceptions.BusinessException;
import com.code.service.responses.BaseResponse;

public class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static BusinessException handleException(Exception e, Long userId, String... additionalInfo) {
	if (e instanceof BusinessException)
	    return (BusinessException) e;

	String loggingMessage = getStackTrace(e);

	if (BasicUtil.hasElements(additionalInfo))
	    for (String info : additionalInfo)
		loggingMessage += System.lineSeparator() + info;

	LoggingUtil.log(loggingMessage, userId);

	return new BusinessException(ErrorMessageCodesEnum.GENERAL);
    }

    public static <T extends BaseResponse> T handleServiceException(Exception e, Long userId, T response, String locale) {

	if (!(e instanceof BusinessException))
	    LoggingUtil.log(getStackTrace(e), userId);

	response.getResponseMetadata().setStatusCode(ServiceResponseStatusesCode.FAILURE.toString());
	response.getResponseMetadata().setErrorCode(e instanceof BusinessException ? e.getMessage() : ErrorMessageCodesEnum.GENERAL.toString());
	response.getResponseMetadata().setErrorMessage(MessageBundleUtil.getMessage(response.getResponseMetadata().getErrorCode(), locale, e instanceof BusinessException ? ((BusinessException) e).getParams() : null));
	return response;
    }

    private static String getStackTrace(Exception e) {
	StringWriter stringWriter = new StringWriter();
	e.printStackTrace(new PrintWriter(stringWriter));
	return stringWriter.toString();
    }
}
