package helpers.file;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ExcelHelper {
    private static final Logger log = LogManager.getLogger(ExcelHelper.class);

    /**
     * Method for parsing Excel file into java collections
     * @param filepath path to testParameters.xlsx file
     * @param sheetName sheet name which the data will be loaded from
     * @return ArrayList containing HashMap of test parameters
     */
    public static ArrayList<HashMap<String, String>> getDataFromExcel(String filepath, String sheetName) {
        ArrayList<HashMap<String, String>> excelFile = new ArrayList<>();
        try {
            log.info("Acquiring test parameters from {} with sheet name {}", filepath, sheetName);
            FileInputStream file = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i);
                HashMap<String, String> currentHash = new LinkedHashMap<>();
                for (int j = 0; j < currentRow.getLastCellNum(); j++) {
                    String cell = new DataFormatter().formatCellValue(currentRow.getCell(j));
                    currentHash.put(headerRow.getCell(j).getStringCellValue(), cell);
                }
                excelFile.add(currentHash);
            }
            file.close();
        } catch (IOException e) {
            log.error("Exception occurred while parsing the excel file into java collections.");
            e.printStackTrace();
        }

        return excelFile;
    }

    /**
     * Method used for parsing test data into data providers
     * @param sheetName name of the sheet in the testData.xlsx file, e.g. DuckDuckGo
     * @return two-dimensional Object representing test data
     */
    public static Object[][] getTestData(String sheetName) {
        Object[][] data = null;
        try {
            String filepath = "src/test/resources/testData.xlsx";
            log.info("Acquiring test data from {} with sheet name {}", filepath, sheetName);
            FileInputStream fileInputStream = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            int rowNum = sheet.getLastRowNum();
            int columnNum = sheet.getRow(0).getLastCellNum();
            data = new Object[rowNum][columnNum];

            for (int i = 0; i < rowNum; i++) {
                for (int j = 0; j < columnNum; j++) {
                    data[i][j] = sheet.getRow(i + 1).getCell(j).toString();
                }
            }
        } catch (IOException e) {
            log.error("Exception occurred while parsing the excel file into test data.");
        }
        return data;
    }

    /**
     * Method for writing inside Excel cell
     */
    public static void writeIntoExcelCell(String excelPath, String sheetName, int rowNumber, int columnNumber, String cellValue) {
        try {
            FileInputStream fileInputStream = new FileInputStream(excelPath);
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row row;
            if (sheet.getRow(rowNumber) == null) {
                row = sheet.createRow(rowNumber);
            } else {
                row = sheet.getRow(rowNumber);
            }
            Cell cell;
            if (row.getCell(columnNumber) == null || row.getCell(columnNumber).getCellType() == CellType.BLANK) {
                cell = row.createCell(columnNumber);
            } else {
                cell = row.getCell(columnNumber);
            }
            cell.setCellValue(cellValue);
            fileInputStream.close();
            FileOutputStream fileOutputStream = new FileOutputStream(excelPath);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
