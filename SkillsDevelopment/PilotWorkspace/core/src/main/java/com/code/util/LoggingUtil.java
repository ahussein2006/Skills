package com.code.util;

import com.code.enums.ChronologyTypesEnum;

public class LoggingUtil {

    private LoggingUtil() {
    }

    // TODO: configuration logging (enable and disable).
    // TODO: console or database.
    public static void logException(Exception e, Long userId, String... additionalInfo) {
	if (e != null) {
	    System.out.println(" <<< " + MultiChronologyCalendarUtil.getSysTimestampString(ChronologyTypesEnum.GREGORIAN) + " >>>" + (userId != null ? " : " + userId : ""));

	    if (BasicUtil.hasElements(additionalInfo))
		for (String info : additionalInfo)
		    System.out.println(info);

	    e.printStackTrace();
	}
    }
}
