package com.code.dal;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.enums.ReportNamesEnum;
import com.code.enums.ReportOutputFormatsEnum;
import com.code.exceptions.RepositoryException;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Service
public class ReportManager {

    @Autowired
    RepositoryManager repositoryManager;

    // TODO: Read from configuration
    private final String reportsRoot = "D:/Skills/SkillsDevelopment/ReportsWorkspace/Reports";
    private final String schemaName = "PILOT";

    public byte[] executeReport(ReportNamesEnum reportFilePath, Map<String, Object> parameters, ReportOutputFormatsEnum reportOutputFormat) throws RepositoryException {
	try {
	    return getReportData(compileReport(reportFilePath.getCode(), false), parameters, reportOutputFormat);
	} catch (Exception e) {
	    throw new RepositoryException(e.getMessage());
	}
    }

    // ----------------------- Report Compiling -----------------

    private JasperReport compileReport(String reportPath, boolean subReportFlag) throws JRException {
	JasperReport jasperReport = JasperCompileManager.compileReport(reportsRoot + reportPath);
	if (subReportFlag)
	    JRSaver.saveObject(jasperReport, reportsRoot + reportPath.replace(".jrxml", ".jasper"));

	JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {

	    @Override
	    public void visitSubreport(JRSubreport subreport) {
		try {
		    String subReportName = subreport.getExpression().getText().replace(".jasper", ".jrxml");
		    subReportName = subReportName.substring(subReportName.lastIndexOf("\"/") + 1, subReportName.length() - 1);
		    compileReport(subReportName, true);
		} catch (Throwable e) {
		    e.printStackTrace();
		}
	    }

	    @Override
	    public void visitTextField(JRTextField textField) {
	    }

	    @Override
	    public void visitComponentElement(JRComponentElement componentElement) {
	    }

	    @Override
	    public void visitGenericElement(JRGenericElement element) {
	    }

	    @Override
	    public void visitBreak(JRBreak arg0) {
	    }

	    @Override
	    public void visitChart(JRChart arg0) {
	    }

	    @Override
	    public void visitCrosstab(JRCrosstab arg0) {
	    }

	    @Override
	    public void visitElementGroup(JRElementGroup arg0) {
	    }

	    @Override
	    public void visitEllipse(JREllipse arg0) {
	    }

	    @Override
	    public void visitFrame(JRFrame arg0) {
	    }

	    @Override
	    public void visitImage(JRImage arg0) {
	    }

	    @Override
	    public void visitLine(JRLine arg0) {
	    }

	    @Override
	    public void visitRectangle(JRRectangle arg0) {
	    }

	    @Override
	    public void visitStaticText(JRStaticText arg0) {
	    }
	});

	return jasperReport;
    }

    // ----------------------- Report Exporting -----------------

    private byte[] getReportData(JasperReport jasperReport, Map<String, Object> parameters, ReportOutputFormatsEnum reportOutputFormat) {
	final List<byte[]> data = new ArrayList<byte[]>();
	setReportCommonParamters(parameters);

	Session session = repositoryManager.getSession();
	session.doWork(new Work() {
	    public void execute(Connection connection) throws SQLException {
		try {
		    data.add(exportReportData(jasperReport, parameters, reportOutputFormat, connection));
		} catch (Throwable e) {
		    throw new SQLException(e.getMessage());
		} finally {
		    session.close();
		}
	    }
	});
	return data.get(0);
    }

    private byte[] exportReportData(JasperReport jasperReport, Map<String, Object> parameters, ReportOutputFormatsEnum reportOutputFormat, Connection connection) throws JRException {
	byte[] data = null;
	JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

	if (ReportOutputFormatsEnum.PDF.equals(reportOutputFormat))
	    data = exportReportDataFormat(jasperPrint, new JRPdfExporter());
	else if (ReportOutputFormatsEnum.DOCX.equals(reportOutputFormat))
	    data = exportReportDataFormat(jasperPrint, new JRDocxExporter());
	else if (ReportOutputFormatsEnum.XLSX.equals(reportOutputFormat))
	    data = exportReportDataFormat(jasperPrint, new JRXlsxExporter());
	return data;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private byte[] exportReportDataFormat(JasperPrint jasperPrint, JRAbstractExporter exporter) throws JRException {
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
	exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
	exporter.exportReport();
	return byteArrayOutputStream.toByteArray();
    }

    // --------------------- Report Configuration ---------------

    private void setReportCommonParamters(Map<String, Object> parameters) {
	parameters.put("P_REPORTS_ROOT", reportsRoot);
	parameters.put("P_SCHEMA_NAME", schemaName);
    }

}