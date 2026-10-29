package org.tinywind.server.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Date;

import static org.junit.Assert.assertEquals;

public class ExcelCompatibilityTest {
    @Test
    public void preservesTypedCellsAndStylesAfterStreamingExport() throws Exception {
        AbstractSinglePageExcel excel = new AbstractSinglePageExcel();
        Row first = excel.createRow();
        excel.setCellValue(first, 0, "Report \u20ac");
        Cell amount = excel.setCellValue(first, 1, 12.5);
        amount.setCellStyle(excel.getCellStyle("0.00"));
        excel.setCellValue(first, 2, true);
        excel.setCellValue(first, 3, Date.valueOf("2026-10-29"), "yyyy-MM-dd");
        excel.setCellValue(first, 4, (String) null);
        for (int index = 1; index < 250; index++) {
            excel.addRow(null, "row-" + index, index);
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        excel.generator(output);

        try (XSSFWorkbook workbook = new XSSFWorkbook(new ByteArrayInputStream(output.toByteArray()))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row restored = sheet.getRow(0);
            assertEquals("Report \u20ac", restored.getCell(0).getStringCellValue());
            assertEquals(CellType.NUMERIC, restored.getCell(1).getCellType());
            assertEquals(12.5, restored.getCell(1).getNumericCellValue(), 0.0);
            assertEquals("0.00", restored.getCell(1).getCellStyle().getDataFormatString());
            assertEquals(CellType.BOOLEAN, restored.getCell(2).getCellType());
            assertEquals(true, restored.getCell(2).getBooleanCellValue());
            assertEquals("2026-10-29", restored.getCell(3).getStringCellValue());
            assertEquals("", restored.getCell(4).getStringCellValue());
            assertEquals(249, sheet.getLastRowNum());
            assertEquals("row-249", sheet.getRow(249).getCell(0).getStringCellValue());
            assertEquals(249.0, sheet.getRow(249).getCell(1).getNumericCellValue(), 0.0);
        }
    }
}
