package com.code.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;

import com.code.config.InjectionManager;
import com.code.dal.RepositoryManager;
import com.code.dal.entities.setup.HijriCalendar;
import com.code.enums.JsonAttributesEnum;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.PatternsEnum;
import com.code.enums.QueryConfigConstants;
import com.code.enums.SeparatorsEnum;

public class MultiChronologyCalendarUtil {

    @Autowired
    private RepositoryManager repositoryManager;

    private static MultiChronologyCalendarUtil instance;

    private static Map<String, HijriCalendar> hijriCalendarMap;
    private static Map<String, HijriCalendar> gregDatesMap;
    private static int hijriCalendarStartYear, hijriCalendarEndYear, gregCalendarStartYear, gregCalendarEndYear;

    private static DecimalFormat twoDigitsDecimalformater = new DecimalFormat(PatternsEnum.TWO_DIGITS.getValue());
    private static SimpleDateFormat shortTimeFormatter = new SimpleDateFormat(PatternsEnum.SHORT_TIME.getValue());
    private static SimpleDateFormat longTimeFormatter = new SimpleDateFormat(PatternsEnum.LONG_TIME.getValue());
    private static SimpleDateFormat hijriDateFormatter = new SimpleDateFormat(PatternsEnum.HIJRI_DATE.getValue());
    private static SimpleDateFormat gregDateFormatter = new SimpleDateFormat(PatternsEnum.GREG_DATE.getValue());

    static {
	instance = new MultiChronologyCalendarUtil();
	InjectionManager.wireServices(BasicUtil.convertObjectToSet(instance));
	init();
    }

    private MultiChronologyCalendarUtil() {
    }

    // -----------------------------------------------------------------------------------------
    private static void init() {
	try {
	    hijriDateFormatter.setLenient(false);
	    gregDateFormatter.setLenient(false);

	    hijriCalendarMap = new HashMap<String, HijriCalendar>();
	    gregDatesMap = new HashMap<String, HijriCalendar>();
	    List<HijriCalendar> hijriCalendarList = instance.repositoryManager.getEntities(HijriCalendar.class, QueryConfigConstants.SP_HijriCalendar_GetHijriCalendar, null);

	    for (HijriCalendar cal : hijriCalendarList) {
		hijriCalendarMap.put(cal.getHijriMonth() + "" + cal.getHijriYear(), cal);
		gregDatesMap.put(getDateString(cal.getHijriMonthEndGregorianDate(), ChronologyTypesEnum.GREGORIAN), cal);
	    }

	    hijriCalendarStartYear = hijriCalendarList.get(0).getHijriYear();
	    hijriCalendarEndYear = hijriCalendarList.get(hijriCalendarList.size() - 1).getHijriYear();

	    int[] startGregDateArray = BasicUtil.getIntSeparatedValues(SeparatorsEnum.SLASH.getValue(), convertHijriToGregDateString("01" + SeparatorsEnum.SLASH.getValue() + "01" + SeparatorsEnum.SLASH.getValue() + hijriCalendarStartYear));
	    gregCalendarStartYear = (startGregDateArray[0] == 1 && startGregDateArray[1] == 1) ? startGregDateArray[2] : startGregDateArray[2] + 1;

	    int[] endGregDateArray = BasicUtil.getIntSeparatedValues(SeparatorsEnum.SLASH.getValue(), getDateString(hijriCalendarList.get(hijriCalendarList.size() - 1).getHijriMonthEndGregorianDate(), ChronologyTypesEnum.GREGORIAN));
	    gregCalendarEndYear = (endGregDateArray[0] == 31 && endGregDateArray[1] == 12) ? endGregDateArray[2] : endGregDateArray[2] - 1;
	} catch (Exception e) {
	    ExceptionUtil.handleException(e, null);
	}
    }

    // ------------------------------------ Multiple Chronology Calendar JSONS -----------------
    public static String getMultiChronologyCalendarData() {
	String[] calNames = new String[] { JsonAttributesEnum.HIJRI_CALENDAR.getValue(), JsonAttributesEnum.GREGORIAN_CALENDAR.getValue() };
	Object[] calValues = new Object[] { getCalendarData(ChronologyTypesEnum.HIJRI), getCalendarData(ChronologyTypesEnum.GREGORIAN) };
	return ContentUtil.convertToJsonString(calNames, calValues);
    }

    private static JsonObject getCalendarData(ChronologyTypesEnum type) {
	JsonArrayBuilder hijriYears = Json.createArrayBuilder();
	JsonArrayBuilder gregorianYears = Json.createArrayBuilder();

	int startValidYear, endValidYear;
	if (type.equals(ChronologyTypesEnum.HIJRI)) {
	    startValidYear = hijriCalendarStartYear;
	    endValidYear = hijriCalendarEndYear;
	} else {
	    startValidYear = gregCalendarStartYear;
	    endValidYear = gregCalendarEndYear;
	}

	for (int year = startValidYear; year <= endValidYear; year++) {
	    JsonArrayBuilder hijriYearMonths = Json.createArrayBuilder();
	    JsonArrayBuilder gregorianYearMonths = Json.createArrayBuilder();

	    for (int month = 1; month <= 12; month++) {
		List<JsonArray> daysArrays = getMonthDays("01" + SeparatorsEnum.SLASH.getValue() + twoDigitsDecimalformater.format(month) + SeparatorsEnum.SLASH.getValue() + year, type);
		hijriYearMonths.add(daysArrays.get(0));
		gregorianYearMonths.add(daysArrays.get(1));
	    }

	    hijriYears.add(hijriYearMonths.build());
	    gregorianYears.add(gregorianYearMonths.build());
	}

	String[] calAttributeNames = new String[] { JsonAttributesEnum.HIJRI_YEARS.getValue(), JsonAttributesEnum.START_VALID_HIJRI_YEAR.getValue(), JsonAttributesEnum.END_VALID_HIJRI_YEAR.getValue(), JsonAttributesEnum.GREGORIAN_YEARS.getValue(), JsonAttributesEnum.START_VALID_GREGORIAN_YEAR.getValue(), JsonAttributesEnum.END_VALID_GREGORIAN_YEAR.getValue() };
	Object[] calAttributeValues = new Object[] { hijriYears, hijriCalendarStartYear, hijriCalendarEndYear, gregorianYears, gregCalendarStartYear, gregCalendarEndYear };

	return ContentUtil.convertToJsonObject(calAttributeNames, calAttributeValues);
    }

    private static List<JsonArray> getMonthDays(String dateString, ChronologyTypesEnum type) {
	List<JsonArray> daysArrays = new ArrayList<JsonArray>();
	JsonArrayBuilder hijriDays = Json.createArrayBuilder();
	JsonArrayBuilder gregDays = Json.createArrayBuilder();

	int[] dateArray = BasicUtil.getIntSeparatedValues(SeparatorsEnum.SLASH.getValue(), dateString);
	int currentMonth = dateArray[1];
	int currentYear = dateArray[2];

	int currentMonthLength, previousMonth, previousMonthLength, nextMonth;

	if (type.equals(ChronologyTypesEnum.HIJRI)) {
	    HijriCalendar currentMonthCal = getHijriCalendar(currentMonth, currentYear);
	    HijriCalendar previousMonthCal = (currentMonth == 1) ? getHijriCalendar(12, currentYear - 1) : getHijriCalendar(currentMonth - 1, currentYear);
	    HijriCalendar nextMonthCal = (currentMonth == 12) ? getHijriCalendar(1, currentYear + 1) : getHijriCalendar(currentMonth + 1, currentYear);

	    currentMonthLength = (currentMonthCal != null ? currentMonthCal.getHijriMonthLength() : 30);
	    previousMonth = (previousMonthCal != null ? previousMonthCal.getHijriMonth() : 0);
	    previousMonthLength = (previousMonthCal != null ? previousMonthCal.getHijriMonthLength() : 30);
	    nextMonth = (nextMonthCal != null ? nextMonthCal.getHijriMonth() : 0);
	} else {
	    currentMonthLength = new DateTime(currentYear, currentMonth, 1, 12, 0).dayOfMonth().getMaximumValue();
	    if (currentMonth == 1) {
		previousMonth = 12;
		previousMonthLength = new DateTime(currentYear - 1, previousMonth, 1, 12, 0).dayOfMonth().getMaximumValue();
	    } else {
		previousMonth = currentMonth - 1;
		previousMonthLength = new DateTime(currentYear, previousMonth, 1, 12, 0).dayOfMonth().getMaximumValue();
	    }
	    nextMonth = (currentMonth == 12) ? 1 : currentMonth + 1;
	}

	// initialize the value with one if this month starts with the first day of week (Sunday),
	// else initialize the value with the previous month day which representing the days of the week before the starting of this month.
	int dayOfWeek = new DateTime(getDate(type.equals(ChronologyTypesEnum.HIJRI) ? convertHijriToGregDateString(dateString) : dateString, ChronologyTypesEnum.GREGORIAN).getTime()).getDayOfWeek() % 7 + 1;
	int value = dayOfWeek == 1 ? 1 : (previousMonthLength - dayOfWeek + 2);

	int month = currentMonth;
	int year = currentYear;

	// draw the month view matrix with dimensions (6 weeks X 7 days of the week)
	for (int i = 0; i < 6; i++) {
	    for (int j = 0; j < 7; j++) {

		if (i == 0 && value > 7 && previousMonth != 0) {
		    month = previousMonth;
		    year = currentMonth == 1 ? currentYear - 1 : currentYear;
		} else if (i >= 4 && value <= 14 && nextMonth != 0) {
		    month = nextMonth;
		    year = currentMonth == 12 ? currentYear + 1 : currentYear;
		} else {
		    month = currentMonth;
		    year = currentYear;
		}

		String dayDateString = twoDigitsDecimalformater.format(value) + SeparatorsEnum.SLASH.getValue() + twoDigitsDecimalformater.format(month) + SeparatorsEnum.SLASH.getValue() + year;

		hijriDays.add(type.equals(ChronologyTypesEnum.HIJRI) ? dayDateString : convertGregToHijriDateString(dateString));
		gregDays.add(type.equals(ChronologyTypesEnum.HIJRI) ? convertHijriToGregDateString(dayDateString) : dateString);

		// if we reach the previous month end, then reset the value which means the starting of this month,
		// or if we reach the end of this month, then reset the value which means the starting of the next month.
		if ((i == 0 && value == previousMonthLength) || (i > 0 && value == currentMonthLength))
		    value = 1;
		else
		    value++;
	    }
	}

	daysArrays.add(hijriDays.build());
	daysArrays.add(gregDays.build());
	return daysArrays;
    }

    // ------------------------------------ Formatting Utilities -------------------------------
    public static Date getDate(String dateString, ChronologyTypesEnum type) {
	try {
	    SimpleDateFormat formatter = type.equals(ChronologyTypesEnum.HIJRI) ? hijriDateFormatter : gregDateFormatter;
	    return formatter.parse(dateString);
	} catch (Exception e) {
	    return null;
	}
    }

    public static String getTimestampString(Date date, ChronologyTypesEnum type, Date... time) {
	String dateString = getDateString(date, type);
	String timeString = formatDate(type.equals(ChronologyTypesEnum.HIJRI) ? time[0] : date, longTimeFormatter);
	return (dateString == null || timeString == null) ? null : dateString + SeparatorsEnum.SPACE.getValue() + timeString;
    }

    public static String getDateTimeString(Date date, ChronologyTypesEnum type, Date... time) {
	String dateString = getDateString(date, type);
	String timeString = formatDate(type.equals(ChronologyTypesEnum.HIJRI) ? time[0] : date, shortTimeFormatter);
	return (dateString == null || timeString == null) ? null : dateString + SeparatorsEnum.SPACE.getValue() + timeString;
    }

    public static String getDateString(Date date, ChronologyTypesEnum type) {
	return formatDate(date, type.equals(ChronologyTypesEnum.HIJRI) ? hijriDateFormatter : gregDateFormatter);
    }

    private static String formatDate(Date date, SimpleDateFormat formatter) {
	return (date == null ? null : formatter.format(date));
    }

    // ------------------------------------ System Dates ---------------------------------------
    public static String getSysTimestampString(ChronologyTypesEnum type) {
	Date gregSysDate = getSysDate(ChronologyTypesEnum.GREGORIAN);
	return getTimestampString(type.equals(ChronologyTypesEnum.HIJRI) ? getSysDate(ChronologyTypesEnum.HIJRI) : gregSysDate, type, gregSysDate);
    }

    public static String getSysDateString(ChronologyTypesEnum type) {
	return getDateString(getSysDate(type), type);
    }

    public static Date getSysDate(ChronologyTypesEnum type) {
	return type.equals(ChronologyTypesEnum.HIJRI) ? convertDate(new Date(), ChronologyTypesEnum.GREGORIAN, ChronologyTypesEnum.HIJRI) : new Date();
    }

    // ------------------------------------ Date Validations and General Utilities -------------
    public static boolean isValidDateString(String dateString, ChronologyTypesEnum type) {
	return isValidDate(getDate(dateString, type), type);
    }

    public static boolean isValidDate(Date date, ChronologyTypesEnum type) {
	if (date == null)
	    return false;

	if (type.equals(ChronologyTypesEnum.HIJRI)) {
	    String[] dateArray = BasicUtil.getSeparatedValues(SeparatorsEnum.SLASH.getValue(), getDateString(date, type));

	    HijriCalendar cal = getHijriCalendar(Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]));

	    if (cal == null || cal.getHijriMonthLength() < Integer.parseInt(dateArray[0]))
		return false;
	}

	return true;
    }

    public static boolean isDateBetween(Date start, Date end, Date check) {
	return check.compareTo(start) * end.compareTo(check) >= 0;
    }

    public static int getDateDay(Date date, ChronologyTypesEnum type) {
	return getDateStringDay(getDateString(date, type));
    }

    public static int getDateMonth(Date date, ChronologyTypesEnum type) {
	return getDateStringMonth(getDateString(date, type));
    }

    public static int getDateYear(Date date, ChronologyTypesEnum type) {
	return getDateStringYear(getDateString(date, type));
    }

    public static int getDateStringDay(String dateString) {
	return Integer.parseInt(BasicUtil.getSeparatedValue(SeparatorsEnum.SLASH.getValue(), dateString, 0));
    }

    public static int getDateStringMonth(String dateString) {
	return Integer.parseInt(BasicUtil.getSeparatedValue(SeparatorsEnum.SLASH.getValue(), dateString, 1));
    }

    public static int getDateStringYear(String dateString) {
	return Integer.parseInt(BasicUtil.getSeparatedValue(SeparatorsEnum.SLASH.getValue(), dateString, 2));
    }

    // ------------------------------------ Date Addition / Subtraction ------------------------
    public static Date addSubDateComponents(Date date, int noOfyears, int noOfMonths, int noOfWeeks, int noOfDays, ChronologyTypesEnum type, boolean... thirtyFlag) {
	return getDate(addSubDateStringComponents(getDateString(date, type), noOfyears, noOfMonths, noOfWeeks, noOfDays, type, thirtyFlag), type);
    }

    public static String addSubDateStringComponents(String dateString, int noOfyears, int noOfMonths, int noOfWeeks, int noOfDays, ChronologyTypesEnum type, boolean... thirtyFlag) {
	boolean hijriThrirtyFlag = thirtyFlag != null ? thirtyFlag[0] : false;
	if (type.equals(ChronologyTypesEnum.HIJRI)) {
	    String monthsDateString = addSubHijriDateStringDays(dateString, (noOfyears * 12 + noOfMonths) * 30, true);
	    return addSubHijriDateStringDays(monthsDateString, (noOfWeeks * 7 + noOfDays), hijriThrirtyFlag);
	} else {
	    return getDateString(addSubGregDateDays(getDate(dateString, type), noOfyears, noOfMonths, noOfWeeks, noOfDays), type);
	}
    }

    public static Date addSubDateDays(Date date, int noOfDays, ChronologyTypesEnum type, boolean... thirtyFlag) {
	return getDate(addSubDateStringDays(getDateString(date, type), noOfDays, type, thirtyFlag), type);
    }

    public static String addSubDateStringDays(String dateString, int noOfDays, ChronologyTypesEnum type, boolean... thirtyFlag) {
	boolean hijriThrirtyFlag = thirtyFlag != null ? thirtyFlag[0] : false;
	return type.equals(ChronologyTypesEnum.HIJRI) ? addSubHijriDateStringDays(dateString, noOfDays, hijriThrirtyFlag) : getDateString(addSubGregDateDays(getDate(dateString, type), 0, 0, 0, noOfDays), type);
    }

    private static String addSubHijriDateStringDays(String hijriDateString, int noOfDays, boolean thirtyFlag) {
	int[] date = BasicUtil.getIntSeparatedValues(SeparatorsEnum.SLASH.getValue(), hijriDateString);

	if (noOfDays >= 0) {
	    while (noOfDays > 0) {
		HijriCalendar hijriCal;
		if (thirtyFlag) {
		    hijriCal = new HijriCalendar();
		    hijriCal.setHijriMonthLength(30);
		} else {
		    hijriCal = getHijriCalendar(date[1], date[2]);
		}

		if (hijriCal.getHijriMonthLength() >= date[0] + noOfDays) {
		    date[0] += noOfDays;
		    noOfDays = 0;
		} else {
		    if (date[1] == 12) {
			date[2]++;
			date[1] = 0;
		    }
		    date[1]++;
		    noOfDays = noOfDays - (hijriCal.getHijriMonthLength() - date[0]) - 1;
		    date[0] = 1;
		}
	    }
	} else {
	    while (noOfDays < 0) {
		if (date[0] > (-1 * noOfDays)) {
		    date[0] += noOfDays;
		    noOfDays = 0;
		} else {
		    if (date[1] == 1) {
			date[2]--;
			date[1] = 13;
		    }
		    date[1]--;

		    HijriCalendar tempCal;
		    if (thirtyFlag) {
			tempCal = new HijriCalendar();
			tempCal.setHijriMonthLength(30);
		    } else {
			tempCal = getHijriCalendar(date[1], date[2]);
		    }

		    date[0] += tempCal.getHijriMonthLength();
		}
	    }
	}

	String newHijriDateString = twoDigitsDecimalformater.format(date[0]) + SeparatorsEnum.SLASH.getValue() + twoDigitsDecimalformater.format(date[1]) + SeparatorsEnum.SLASH.getValue() + date[2];
	if (!isValidDateString(newHijriDateString, ChronologyTypesEnum.HIJRI)) {
	    newHijriDateString = "29" + SeparatorsEnum.SLASH.getValue() + twoDigitsDecimalformater.format(date[1]) + SeparatorsEnum.SLASH.getValue() + date[2];
	}
	return newHijriDateString;
    }

    private static Date addSubGregDateDays(Date gregDate, int noOfyears, int noOfMonths, int noOfWeeks, int noOfDays) {
	DateTime resultDate = new DateTime(gregDate.getTime());
	resultDate = noOfyears >= 0 ? resultDate.plusYears(noOfyears) : resultDate.minusYears(-1 * noOfyears);
	resultDate = noOfMonths >= 0 ? resultDate.plusMonths(noOfMonths) : resultDate.minusMonths(-1 * noOfMonths);
	resultDate = noOfWeeks >= 0 ? resultDate.plusWeeks(noOfWeeks) : resultDate.minusWeeks(-1 * noOfWeeks);
	resultDate = noOfDays >= 0 ? resultDate.plusDays(noOfDays) : resultDate.minusDays(-1 * noOfDays);

	return getDate(DateTimeFormat.forPattern(PatternsEnum.GREG_DATE.getValue()).print(resultDate), ChronologyTypesEnum.GREGORIAN);
    }

    // ------------------------------------ Date Difference ------------------------------------
    public static int calcDateDiff(Date startDate, Date endDate, ChronologyTypesEnum type) {
	return calcDateStringDiff(getDateString(startDate, type), getDateString(endDate, type), type);
    }

    public static int calcDateStringDiff(String startDateString, String endDateString, ChronologyTypesEnum type) {
	return type.equals(ChronologyTypesEnum.HIJRI) ? calcHijriDateStringDiff(startDateString, endDateString) : calcGregDateDiff(getDate(startDateString, type), getDate(endDateString, type));
    }

    private static int calcHijriDateStringDiff(String hijriStartDateString, String hijriEndDateString) {
	return calcGregDateDiff(getDate(convertHijriToGregDateString(hijriStartDateString), ChronologyTypesEnum.GREGORIAN), getDate(convertHijriToGregDateString(hijriEndDateString), ChronologyTypesEnum.GREGORIAN));
    }

    private static int calcGregDateDiff(Date gregStartDate, Date gregEndDate) {
	DateTime startGregDate = new DateTime(gregStartDate.getTime());
	DateTime endGregDate = new DateTime(gregEndDate.getTime());
	Days d = Days.daysBetween(startGregDate.toLocalDate(), endGregDate.toLocalDate());
	return d.getDays();
    }

    public static int[] calcDateDiffInMonthsAndDays(Date startDate, Date endDate, ChronologyTypesEnum type) {
	return calcDateStringDiffInMonthsAndDays(getDateString(startDate, type), getDateString(endDate, type), type);
    }

    public static int[] calcDateStringDiffInMonthsAndDays(String startDateString, String endDateString, ChronologyTypesEnum type) {
	return type.equals(ChronologyTypesEnum.HIJRI) ? calcHijriDateStringDiffInMonthsAndDays(startDateString, endDateString) : calcGregDateDiffInMonthsAndDays(getDate(startDateString, type), getDate(endDateString, type));
    }

    private static int[] calcHijriDateStringDiffInMonthsAndDays(String hijriStartDateString, String hijriEndDateString) {
	int[] diff = new int[] { 0, 0 };
	int[] startDateArray = BasicUtil.getIntSeparatedValues(SeparatorsEnum.SLASH.getValue(), hijriStartDateString);
	int[] endDateArray = BasicUtil.getIntSeparatedValues(SeparatorsEnum.SLASH.getValue(), hijriEndDateString);

	// First we get the months between the two dates but with excluding both start and end month
	if (endDateArray[2] - startDateArray[2] >= 1) {
	    diff[1] = (endDateArray[2] - startDateArray[2] - 1) * 12;
	    diff[1] += 12 - startDateArray[1];
	    diff[1] += endDateArray[1] - 1;
	} else {
	    int diffMonths = endDateArray[1] - startDateArray[1] - 1;
	    if (diffMonths > 0)
		diff[1] = diffMonths;
	}

	// Now we are calculating the days count in the starting month and the ending month

	// If the starting months is the ending month
	if (endDateArray[2] == startDateArray[2] && endDateArray[1] == startDateArray[1]) {
	    diff[0] = endDateArray[0] - startDateArray[0] + 1;

	    // If the difference in days is the whole month length then its considered as one month
	    int startMonthActualLength = getHijriCalendar(startDateArray[1], startDateArray[2]).getHijriMonthLength();
	    if (startMonthActualLength == diff[0]) {
		diff[0] = 0;
		diff[1]++;
	    }
	} else {
	    diff[0] = 30 - startDateArray[0] + 1;
	    diff[0] += endDateArray[0];

	    if (diff[0] >= 30) {
		diff[0] -= 30;
		diff[1]++;

		// If the difference in days is the whole month length then its considered as one month
		if (diff[0] == getHijriCalendar(endDateArray[1], endDateArray[2]).getHijriMonthLength()) {
		    diff[0] = 0;
		    diff[1]++;
		}
	    } else { // Difference in days are less than 30 so it's counted in days based on month (The month before the last month) length
		int monthActualLength;
		if (endDateArray[1] == 1)
		    monthActualLength = getHijriCalendar(12, endDateArray[2] - 1).getHijriMonthLength();
		else
		    monthActualLength = getHijriCalendar(endDateArray[1] - 1, endDateArray[2]).getHijriMonthLength();

		diff[0] = monthActualLength - startDateArray[0] + 1;
		diff[0] += endDateArray[0];
	    }
	}

	return diff;
    }

    private static int[] calcGregDateDiffInMonthsAndDays(Date gregStartDate, Date gregEndDate) {
	return null;
    }

    // ------------------------------------ Date Conversion ------------------------------------
    public static Date convertDate(Date date, ChronologyTypesEnum fromType, ChronologyTypesEnum toType) {
	return getDate(convertDateString(getDateString(date, fromType), fromType, toType), toType);
    }

    public static String convertDateString(String dateString, ChronologyTypesEnum fromType, ChronologyTypesEnum toType) {
	if (fromType.equals(ChronologyTypesEnum.HIJRI) && toType.equals(ChronologyTypesEnum.GREGORIAN))
	    return convertHijriToGregDateString(dateString);
	else if (fromType.equals(ChronologyTypesEnum.GREGORIAN) && toType.equals(ChronologyTypesEnum.HIJRI))
	    return convertGregToHijriDateString(dateString);
	else
	    return null;
    }

    private static String convertHijriToGregDateString(String hijriDateString) {
	try {
	    String[] hijriDateArray = BasicUtil.getSeparatedValues(SeparatorsEnum.SLASH.getValue(), hijriDateString);
	    HijriCalendar hijriDateCal = getHijriCalendar(Integer.parseInt(hijriDateArray[1]), Integer.parseInt(hijriDateArray[2]));
	    return getDateString(addSubGregDateDays(hijriDateCal.getHijriMonthEndGregorianDate(), 0, 0, 0, Integer.parseInt(hijriDateArray[0]) - hijriDateCal.getHijriMonthLength()), ChronologyTypesEnum.GREGORIAN);
	} catch (Exception e) {
	    return null;
	}
    }

    private static String convertGregToHijriDateString(String gregDateString) {
	try {
	    HijriCalendar endCal = gregDatesMap.get(gregDateString);
	    if (endCal == null) {
		Date gregDate = getDate(gregDateString, ChronologyTypesEnum.GREGORIAN);
		for (int i = 0; i < 31; i++) {
		    gregDate = addSubGregDateDays(gregDate, 0, 0, 0, 1);
		    endCal = gregDatesMap.get(getDateString(gregDate, ChronologyTypesEnum.GREGORIAN));
		    if (endCal != null)
			break;
		}
	    }

	    String monthYear = SeparatorsEnum.SLASH.getValue() + twoDigitsDecimalformater.format(endCal.getHijriMonth()) + SeparatorsEnum.SLASH.getValue() + endCal.getHijriYear();

	    int daysDiff = calcGregDateDiff(getDate(gregDateString, ChronologyTypesEnum.GREGORIAN), endCal.getHijriMonthEndGregorianDate());
	    int variance = endCal.getHijriMonthLength() - daysDiff;
	    if (variance <= 0) {
		variance--;
		return addSubHijriDateStringDays("01" + monthYear, variance, false);
	    }

	    return twoDigitsDecimalformater.format(variance) + monthYear;
	} catch (Exception e) {
	    return null;
	}
    }

    // ------------------------------------ Date Lookup ----------------------------------------
    private static HijriCalendar getHijriCalendar(int hijriMonth, int hijriYear) {
	return hijriCalendarMap.get(hijriMonth + "" + hijriYear);
    }
}