package com.code.util;
/*import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.code.enums.ErrorMessageCodesEnum;
import com.code.enums.FileTypesEnum;
import com.code.enums.SeparatorsEnum;
import com.code.exceptions.BusinessException;*/

//TODO: Implement
public class SpreadsheetUtil {

    /*
     * private final static String CONCATENATED_ALLOWED_SHEET_FILE_TYPES = BasicUtil.constructSeparatedValues(SeparatorsEnum.DASH.getValue(), FileTypesEnum.XLS.getValue(), FileTypesEnum.XLSX.getValue());
     * private final static String CONCATENATED_ALLOWED_SHEET_CELL_TYPES = BasicUtil.constructSeparatedValues(SeparatorsEnum.DASH.getValue(), CellType.STRING.toString(), CellType.NUMERIC.toString(), CellType.BLANK.toString());
     * 
     * private SpreadsheetUtil() {
     * 
     * }
     * 
     * public static List<List<String>> parseSpreadsheet(InputStream sheetFileContent, String sheetFileType, int sheetIndex, int maxRowsNumber, int columnsNumber, boolean hasHeader) throws BusinessException {
     * try (Workbook workbook = constructWorkBook(sheetFileContent, sheetFileType)) {
     * Sheet sheet = getWorkBookSheet(workbook, sheetIndex);
     * validateSheetRowsNumber(sheet, maxRowsNumber, hasHeader);
     * Iterator<Row> rowIterator = getSheetIterator(sheet, hasHeader);
     * 
     * List<List<String>> sheetRows = new ArrayList<List<String>>();
     * while (rowIterator.hasNext()) {
     * Row row = rowIterator.next();
     * validateRowColumnsNumber(row, columnsNumber);
     * 
     * List<String> rowCells = new ArrayList<String>();
     * for (int i = 0; i < columnsNumber; i++)
     * rowCells.add(getCellValue(row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
     * 
     * sheetRows.add(rowCells);
     * }
     * return sheetRows;
     * } catch (Exception e) {
     * ExceptionUtil.handleException(e, SpreadsheetUtil.class.getCanonicalName());
     * return null;
     * }
     * }
     * 
     * private static Workbook constructWorkBook(InputStream sheetFileContent, String sheetFileType) throws BusinessException, IOException {
     * if (sheetFileType.equalsIgnoreCase(FileTypesEnum.XLSX.getValue()))
     * return new XSSFWorkbook(sheetFileContent);
     * else if (sheetFileType.equalsIgnoreCase(FileTypesEnum.XLS.getValue()))
     * return new HSSFWorkbook(sheetFileContent);
     * else
     * throw new BusinessException(ErrorMessageCodesEnum.SHEET_UNSUPPORTED_FILE_FORMAT.getValue(), new Object[] { CONCATENATED_ALLOWED_SHEET_FILE_TYPES });
     * }
     * 
     * private static Sheet getWorkBookSheet(Workbook workbook, int sheetIndex) throws BusinessException {
     * if (sheetIndex < 0 || sheetIndex >= workbook.getNumberOfSheets())
     * throw new BusinessException(ErrorMessageCodesEnum.SHEET_INDEX_OUT_OF_BOUND.getValue());
     * return workbook.getSheetAt(sheetIndex);
     * }
     * 
     * private static void validateSheetRowsNumber(Sheet sheet, int maxRowsNumber, boolean hasHeader) throws BusinessException {
     * if (maxRowsNumber <= 0 || sheet.getPhysicalNumberOfRows() > (maxRowsNumber + (hasHeader ? 1 : 0)))
     * throw new BusinessException(ErrorMessageCodesEnum.SHEET_ROW_OUT_OF_BOUND.getValue(), new Object[] { maxRowsNumber });
     * }
     * 
     * private static Iterator<Row> getSheetIterator(Sheet sheet, boolean hasHeader) {
     * Iterator<Row> rowIterator = sheet.iterator();
     * if (hasHeader && rowIterator.hasNext())
     * rowIterator.next(); // to skip first row (the header)
     * return rowIterator;
     * }
     * 
     * private static void validateRowColumnsNumber(Row row, int columnsNumber) throws BusinessException {
     * if (row.getPhysicalNumberOfCells() > columnsNumber)
     * throw new BusinessException(ErrorMessageCodesEnum.SHEET_COLUMN_OUT_OF_BOUND.getValue(), new Object[] { columnsNumber });
     * }
     * 
     * private static String getCellValue(Cell cell) throws BusinessException {
     * String cellValue = null;
     * if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
     * cell.setCellType(CellType.STRING);
     * cellValue = new BigDecimal(cell.getStringCellValue().trim()).toPlainString();
     * } else if (cell.getCellTypeEnum().equals(CellType.STRING)) {
     * cellValue = cell.getStringCellValue().trim();
     * } else if (cell.getCellTypeEnum().equals(CellType.BLANK)) {
     * cellValue = null;
     * } else {
     * throw new BusinessException(ErrorMessageCodesEnum.SHEET_UNSUPPORTED_CELL_TYPE.getValue(), new Object[] { cell.getRowIndex() + SeparatorsEnum.COLON.getValue() + cell.getColumnIndex(), CONCATENATED_ALLOWED_SHEET_CELL_TYPES });
     * }
     * return cellValue;
     * }
     */

}
