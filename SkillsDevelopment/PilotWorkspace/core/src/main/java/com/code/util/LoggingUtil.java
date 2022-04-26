package com.code.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.code.enums.PatternsEnum;

public class LoggingUtil {

    private LoggingUtil() {
    }

    // TODO: configuration logging (enable and disable).
    // TODO: console or database.
    // TODO: DateUtil.
    public static void logException(Exception e, String className, String... additionalInfo) {
	if (e != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat(PatternsEnum.FULL_TIMESTAMP.getValue());
	    System.out.println(" <<< " + sdf.format(new Date()) + " >>> : " + className);

	    if (BasicUtil.hasElements(additionalInfo))
		for (String info : additionalInfo)
		    System.out.println(info);

	    e.printStackTrace();
	}
    }
}
