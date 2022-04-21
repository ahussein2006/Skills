package com.code.integration.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.business.TestBusiness;
import com.code.dal.entities.config.Configuration;
import com.code.enums.MediaTypeConstants;
import com.code.enums.ReportOutputFormatsEnum;

@Path("/test")
@Consumes(MediaTypeConstants.APPLICATION_JSON)
@Produces(MediaTypeConstants.APPLICATION_JSON)
public class TestService {

    @Autowired
    private TestBusiness testBusiness;

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
