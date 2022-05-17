package com.code.integration.services;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.business.TestBusiness;
import com.code.dal.entities.config.Configuration;
import com.code.enums.ChronologyTypesEnum;
import com.code.enums.MediaTypeConstants;
import com.code.enums.ReportOutputFormatsEnum;
import com.code.util.MultiChronologyCalendarUtil;

@Path("/test")
@Consumes(MediaTypeConstants.APPLICATION_JSON)
@Produces(MediaTypeConstants.APPLICATION_JSON)
public class TestService {

    @Autowired
    private TestBusiness testBusiness;

    @GET
    @Path("/cal/data")
    public String getMultiChronologyCalendarData() {
	return MultiChronologyCalendarUtil.getMultiChronologyCalendarData();
    }

    @GET
    @Path("/cal/format/date/{date}")
    public String getDate(@PathParam("date") String dateString, @QueryParam("type") String type) {
	dateString = dateString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.getDateString(date, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/format/timestamp/{date}")
    public String getTimestamp(@PathParam("date") String timestampString, @QueryParam("type") String type) {
	timestampString = timestampString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(timestampString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.getTimestampString(date, ChronologyTypesEnum.valueOf(type), new Date());
    }

    @GET
    @Path("/cal/format/dateTime/{date}")
    public String getDateTime(@PathParam("date") String dateTimeString, @QueryParam("type") String type) {
	dateTimeString = dateTimeString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateTimeString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.getDateTimeString(date, ChronologyTypesEnum.valueOf(type), new Date());
    }

    @GET
    @Path("/cal/sysDate/date")
    public String getSysDate(@QueryParam("type") String type) {
	Date date = MultiChronologyCalendarUtil.getSysDate(ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.getDateString(date, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/sysDate/dateString")
    public String getSysDateString(@QueryParam("type") String type) {
	return MultiChronologyCalendarUtil.getSysDateString(ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/sysDate/timestamp")
    public String getSysTimestampString(@QueryParam("type") String type) {
	return MultiChronologyCalendarUtil.getSysTimestampString(ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/convert/{date}")
    public String convertDate(@PathParam("date") String dateString, @QueryParam("fromType") String fromType, @QueryParam("toType") String toType) {
	dateString = dateString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateString, ChronologyTypesEnum.valueOf(fromType));
	Date convertedDate = MultiChronologyCalendarUtil.convertDate(date, ChronologyTypesEnum.valueOf(fromType), ChronologyTypesEnum.valueOf(toType));
	return MultiChronologyCalendarUtil.getDateString(convertedDate, ChronologyTypesEnum.valueOf(toType));
    }

    @GET
    @Path("/cal/util/day/{date}")
    public int getDateDay(@PathParam("date") String dateString, @QueryParam("type") String type) {
	dateString = dateString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.getDateDay(date, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/util/month/{date}")
    public int getDateMonth(@PathParam("date") String dateString, @QueryParam("type") String type) {
	dateString = dateString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.getDateMonth(date, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/util/year/{date}")
    public int getDateYear(@PathParam("date") String dateString, @QueryParam("type") String type) {
	dateString = dateString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.getDateYear(date, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/util/validate/{date}")
    public boolean isValidDateString(@PathParam("date") String dateString, @QueryParam("type") String type) {
	dateString = dateString.replaceAll("-", "/");
	return MultiChronologyCalendarUtil.isValidDateString(dateString, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/util/between/{start}/{end}/{check}")
    public boolean isDateBetween(@PathParam("start") String startDateString, @PathParam("end") String endDateString, @PathParam("check") String checkDateString, @QueryParam("type") String type) {
	startDateString = startDateString.replaceAll("-", "/");
	endDateString = endDateString.replaceAll("-", "/");
	checkDateString = checkDateString.replaceAll("-", "/");
	Date start = MultiChronologyCalendarUtil.getDate(startDateString, ChronologyTypesEnum.valueOf(type));
	Date end = MultiChronologyCalendarUtil.getDate(endDateString, ChronologyTypesEnum.valueOf(type));
	Date check = MultiChronologyCalendarUtil.getDate(checkDateString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.isDateBetween(start, end, check);
    }

    @GET
    @Path("/cal/addSub/{date}/{y}/{m}/{w}/{d}")
    public String addSubDateComponents(@PathParam("date") String dateString, @PathParam("y") int y, @PathParam("m") int m, @PathParam("w") int w, @PathParam("d") int d, @QueryParam("type") String type, @QueryParam("thirty") int thirty) {
	dateString = dateString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateString, ChronologyTypesEnum.valueOf(type));
	Date result = MultiChronologyCalendarUtil.addSubDateComponents(date, y, m, w, d, ChronologyTypesEnum.valueOf(type), thirty == 1);
	return MultiChronologyCalendarUtil.getDateString(result, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/addSubDays/{date}/{d}")
    public String addSubDateDays(@PathParam("date") String dateString, @PathParam("d") int d, @QueryParam("type") String type, @QueryParam("thirty") int thirty) {
	dateString = dateString.replaceAll("-", "/");
	Date date = MultiChronologyCalendarUtil.getDate(dateString, ChronologyTypesEnum.valueOf(type));
	Date result = MultiChronologyCalendarUtil.addSubDateDays(date, d, ChronologyTypesEnum.valueOf(type), thirty == 1);
	return MultiChronologyCalendarUtil.getDateString(result, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/diff/{start}/{end}")
    public int calcDateDiff(@PathParam("start") String startDateString, @PathParam("end") String endDateString, @QueryParam("type") String type) {
	startDateString = startDateString.replaceAll("-", "/");
	endDateString = endDateString.replaceAll("-", "/");
	Date start = MultiChronologyCalendarUtil.getDate(startDateString, ChronologyTypesEnum.valueOf(type));
	Date end = MultiChronologyCalendarUtil.getDate(endDateString, ChronologyTypesEnum.valueOf(type));
	return MultiChronologyCalendarUtil.calcDateDiff(start, end, ChronologyTypesEnum.valueOf(type));
    }

    @GET
    @Path("/cal/diffMD/{start}/{end}")
    public String calcDateDiffInMonthsAndDays(@PathParam("start") String startDateString, @PathParam("end") String endDateString, @QueryParam("type") String type) {
	startDateString = startDateString.replaceAll("-", "/");
	endDateString = endDateString.replaceAll("-", "/");
	Date start = MultiChronologyCalendarUtil.getDate(startDateString, ChronologyTypesEnum.valueOf(type));
	Date end = MultiChronologyCalendarUtil.getDate(endDateString, ChronologyTypesEnum.valueOf(type));
	int[] d = MultiChronologyCalendarUtil.calcDateDiffInMonthsAndDays(start, end, ChronologyTypesEnum.valueOf(type));
	return d == null ? "NO" : d[0] + "-" + d[1];
    }

    @GET
    @Path("/config/{code}")
    public Configuration getConfigByCode(@PathParam("code") String code) {
	return testBusiness.getConfigByCode(code);
    }

    @POST
    @Path("/config")
    public void addConfig(Configuration config) {
	testBusiness.addConfig(config);
    }

    @GET
    @Path("/report/PDF")
    @Produces(MediaTypeConstants.APPLICATION_PDF)
    public byte[] getReportPDFData() {
	return testBusiness.getReportData(ReportOutputFormatsEnum.PDF);
    }

    @GET
    @Path("/report/DOCX")
    @Produces(MediaTypeConstants.APPLICATION_DOCX)
    public byte[] getReportDOCXData() {
	return testBusiness.getReportData(ReportOutputFormatsEnum.DOCX);
    }

    @GET
    @Path("/report/XLSX")
    @Produces(MediaTypeConstants.APPLICATION_XLSX)
    public byte[] getReportXLSXData() {
	return testBusiness.getReportData(ReportOutputFormatsEnum.XLSX);
    }
}
