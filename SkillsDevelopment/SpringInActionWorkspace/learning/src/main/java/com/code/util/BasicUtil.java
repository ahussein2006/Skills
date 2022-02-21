package com.code.util;

import java.lang.StackWalker.StackFrame;

public class BasicUtil {

	private BasicUtil() {

	}

	public static String getCallerMethod() {
		StackFrame sf = StackWalker.getInstance().walk(stackFrames -> stackFrames
						.filter(stackFrame -> stackFrame.getClassName().contains("com.code."))
						.skip(2)
						.findFirst()).get();
		return sf.getClassName() + "." + sf.getMethodName();
	}
}
