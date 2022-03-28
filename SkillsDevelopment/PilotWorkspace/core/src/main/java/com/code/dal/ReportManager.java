package com.code.dal;

/*import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.enums.ReportsOutputsFormatsEnum;
import com.code.exceptions.BusinessException;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
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
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.util.JRSaver;
*/
//@Service
public class ReportManager {
/*
    @Autowired
    RepositoryManager repositoryManager;

    private ReportManager() {
    }

    public static byte[] getReportData(final String reportFilePath, final Map<String, Object> parameters, final String reportsRoot, final String schemaName) throws BusinessException {
	try {
	    return getReportData(reportFilePath, parameters, ReportsOutputsFormatsEnum.PDF, reportsRoot, schemaName);
	} catch (SQLException e) {
	    throw new BusinessException("error_general");
	}
    }

    public static byte[] getRTFReportData(final String reportFilePath, final Map<String, Object> parameters, final String reportsRoot, final String schemaName) throws BusinessException {
	try {
	    return getReportData(reportFilePath, parameters, ReportsOutputsFormatsEnum.RTF, reportsRoot, schemaName);
	} catch (SQLException e) {
	    throw new BusinessException("error_general");
	}
    }

    public static byte[] getExcelReportData(final String reportFilePath, final Map<String, Object> parameters, final String reportsRoot, final String schemaName) throws BusinessException {
	try {
	    return getReportData(reportFilePath, parameters, ReportsOutputsFormatsEnum.EXCEL, reportsRoot, schemaName);
	} catch (SQLException e) {
	    throw new BusinessException("error_general");
	}
    }

    private byte[] getReportData(final String reportFilePath, final Map<String, Object> parameters, final ReportsOutputsFormatsEnum reportOutputFormat, final String reportsRoot, final String schemaName) throws SQLException {
	final List<byte[]> data = new ArrayList<byte[]>();

	Session session = repositoryManager.getSession();
	session.doWork(new Work() {
	    public void execute(Connection con) throws SQLException {
		try {
		    parameters.put("P_REPORTS_ROOT", reportsRoot);
		    parameters.put("P_SCHEMA_NAME", schemaName);
		    JasperReport jasperReport = compileReport(reportFilePath, reportsRoot);
		    compileSubReports(jasperReport, reportsRoot);
		    if (ReportsOutputsFormatsEnum.PDF.equals(reportOutputFormat))
			data.add(JasperRunManager.runReportToPdf(jasperReport, parameters, con));
		    else if (ReportsOutputsFormatsEnum.DOCX.equals(reportOutputFormat))
			data.add(exportReportToDocxFormat(JasperFillManager.fillReport(jasperReport, parameters, con)));
		    else if (ReportsOutputsFormatsEnum.RTF.equals(reportOutputFormat))
			data.add(exportReportToRtfFormat(JasperFillManager.fillReport(jasperReport, parameters, con)));
		    else if (ReportsOutputsFormatsEnum.EXCEL.equals(reportOutputFormat))
			data.add(exportReportToExcelFormat(JasperFillManager.fillReport(jasperReport, parameters, con)));
		} catch (Exception e) {
		    e.printStackTrace();
		    throw new SQLException(e.getMessage());
		} catch (Throwable e) {
		    e.printStackTrace();
		    throw new SQLException(e.getMessage());
		} finally {
		    session.close();
		}
	    }
	});

	return data.get(0);
    }

    private static JasperReport compileReport(String reportPath, String reportsRoot) throws JRException {
	return JasperCompileManager.compileReport(reportsRoot + reportPath);
    }

    
     // Recursively compile sub-reports
    
    private static JasperReport compileSubReports(JasperReport jasperReport, String reportsRoot) {
	JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {
	    @Override
	    public void visitSubreport(JRSubreport subreport) {
		try {
		    String subReportName = subreport.getExpression().getText().replace(".jasper", ".jrxml");
		    subReportName = subReportName.substring(subReportName.lastIndexOf("\"/") + 1, subReportName.length() - 1);

		    JasperReport compiledSubRep = compileReport(subReportName, reportsRoot);
		    JRSaver.saveObject(compiledSubRep, reportsRoot + subReportName.replace(".jrxml", ".jasper"));
		    compileSubReports(compiledSubRep, reportsRoot);
		} catch (JRException e) {
		    e.printStackTrace();
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

    private static byte[] exportReportToDocxFormat(JasperPrint jasperPrint) throws JRException {
	JRDocxExporter exporter = new JRDocxExporter();
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
	exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
	exporter.exportReport();
	return byteArrayOutputStream.toByteArray();
    }

    private static byte[] exportReportToRtfFormat(JasperPrint jasperPrint) throws JRException {
	JRRtfExporter exporter = new JRRtfExporter();
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
	exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
	exporter.exportReport();
	return byteArrayOutputStream.toByteArray();
    }

    private static byte[] exportReportToExcelFormat(JasperPrint jasperPrint) throws JRException {
	JRXlsxExporter exporter = new JRXlsxExporter();
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
	exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
	exporter.setParameter(JRXlsExporterParameter.CHARACTER_ENCODING, "UTF-8");
	exporter.exportReport();
	return byteArrayOutputStream.toByteArray();
    }
    */
}