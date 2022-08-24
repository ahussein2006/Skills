package com.code.exceptions;

import com.code.enums.ErrorMessageCodesEnum;

public class BusinessException extends Exception {
    private Object[] params = null;

    public BusinessException(ErrorMessageCodesEnum messageCode) {
	super(messageCode.toString());
    }

    public BusinessException(ErrorMessageCodesEnum messageCode, Object[] params) {
	super(messageCode.toString());
	this.params = params;
    }

    public Object[] getParams() {
	return params;
    }

    public void setParams(Object[] params) {
	this.params = params;
    }
}