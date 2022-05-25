package com.code.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.code.config.InjectionManager;
import com.code.enums.SeparatorsEnum;
import com.code.exceptions.BusinessException;

public class ReflictionUtil {
    public static void invokeMethod(String target, Object... params) throws BusinessException {
	try {
	    Class<?> targetClass = Class.forName(target.substring(0, target.lastIndexOf(SeparatorsEnum.DOT.getValue())));
	    Method targetMethod = targetClass.getDeclaredMethod(target.substring(target.lastIndexOf(SeparatorsEnum.DOT.getValue()) + 1, target.length()), Object[].class);
	    targetMethod.setAccessible(true);

	    Object targetObject = targetClass.getConstructor().newInstance();
	    InjectionManager.wireServices(BasicUtil.convertObjectToSet(targetObject));

	    targetMethod.invoke(targetObject, new Object[] { params });
	} catch (Exception e) {
	    throw ExceptionUtil.handleException(e instanceof InvocationTargetException ? (Exception) ((InvocationTargetException) e).getTargetException() : e, null);
	}
    }

}
