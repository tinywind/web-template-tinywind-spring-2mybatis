package org.tinywind.server.util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Arrays;
import java.util.List;

public class AbstractSinglePageExcel extends AbstractExcel {

    protected Sheet sheet;
    protected int rownum;

    public AbstractSinglePageExcel() {
        super();
        this.sheet = workbook.createSheet();
    }

    public Row createRow() {
        return sheet.createRow(rownum++);
    }

    public Row addRow(CellStyle style, Object... columns) {
        return this.addRow(style, Arrays.asList(columns));
    }

    public Row addRow(CellStyle style, List<Object> columns) {
        Row row = this.createRow();
        int column = 0;
        for (Object c : columns) {
            Cell cell = row.createCell(column++);
            if (c instanceof Number) {
                cell.setCellValue(((Number) c).doubleValue());
            } else {
                cell.setCellValue(c.toString());
            }

            if (style != null)
                cell.setCellStyle(style);
        }
        return row;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public int getRownum() {
        return rownum;
    }

    public void setRownum(int rownum) {
        this.rownum = rownum;
    }
}
