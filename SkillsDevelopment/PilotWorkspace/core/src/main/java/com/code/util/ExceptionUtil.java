package com.code.util;

import com.code.enums.ErrorMessageCodesEnum;
import com.code.exceptions.BusinessException;

public class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static <T> BusinessException handleException(Exception e, Long userId, String... additionalInfo) {
	if (e instanceof BusinessException)
	    return (BusinessException) e;

	LoggingUtil.logException(e, userId, additionalInfo);
	return new BusinessException(ErrorMessageCodesEnum.GENERAL.getValue());
    }
}
