package com.code.business.util;

import com.code.enums.ErrorMessageCodesEnum;
import com.code.exceptions.BusinessException;

public class ExceptionUtil {

    private ExceptionUtil() {

    }

    public static void handleException(Exception e, String className, String... additionalInfo) throws BusinessException {
	if (e instanceof BusinessException)
	    throw (BusinessException) e;

	LoggingUtil.logException(e, className, additionalInfo);
	throw new BusinessException(ErrorMessageCodesEnum.GENERAL.getValue());
    }
}
