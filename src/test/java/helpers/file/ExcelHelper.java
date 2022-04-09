package helpers.file;

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
    /**
     * Method for parsing Excel file into java collections
     */
    public static ArrayList<HashMap<String, String>> getDataFromExcel(String filepath, String sheetName) {
        ArrayList<HashMap<String, String>> excelFile = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(filepath);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            Row HeaderRow = sheet.getRow(0);

            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row currentRow = sheet.getRow(i);
                HashMap<String, String> currentHash = new LinkedHashMap<>();
                for (int j = 0; j < currentRow.getLastCellNum(); j++) {
                    String cell = new DataFormatter().formatCellValue(currentRow.getCell(j));
                    currentHash.put(HeaderRow.getCell(j).getStringCellValue(), cell);
                }
                excelFile.add(currentHash);
            }
            file.close();
        } catch (Exception e) {
            System.out.println("Exception occurred while parsing the excel file into java collections.");
            e.printStackTrace();
        }

        return excelFile;
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

    public static Object[][] getTestData(String sheetName) {
        Object[][] data = null;
        try {
            FileInputStream fileInputStream = new FileInputStream("src/test/resources/testData.xlsx");
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
            e.printStackTrace();
        }
        return data;
    }
}
