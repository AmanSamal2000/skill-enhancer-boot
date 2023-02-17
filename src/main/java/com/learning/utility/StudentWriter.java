package com.learning.utility;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.*;

import com.learning.entity.mysql.StudentEntity;
import org.springframework.stereotype.Component;

@Component
public class StudentWriter {

    public void createStudentSheet(Workbook workbook, List<StudentEntity> studentEntityList){
        String[] headers = {"Id", "Name", "Contact Details", "Qualification.", "Email"};

        // create a Sheet
        Sheet sheet = workbook.createSheet("Student");

        // method call for headerCell styling
        CellStyle headerCellStyle = getHeaderCellStyle(workbook);

        // method call for header row creation
        createHeaderRow(headers, sheet, headerCellStyle);

        // method call for data rows creation
        createDataRows(studentEntityList, workbook, sheet);

        // Resize all columns to fit the content size
        for(int index = 0; index < headers.length; index++) {
            sheet.autoSizeColumn(index);
        }

        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = new FileOutputStream(".\\resources\\student-db-data.xlsx");

            workbook.write(fileOutputStream);

            fileOutputStream.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private static void createDataRows(List<StudentEntity> studentEntityList, Workbook workbook, Sheet sheet) {
        //setting cell style
        CellStyle dataCellStyle =  workbook.createCellStyle();
        dataCellStyle.setAlignment(HorizontalAlignment.CENTER);
        dataCellStyle.setFillForegroundColor((short) 22);
        dataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//FillPatternType.SOLID_FOREGROUND
        dataCellStyle.setBorderBottom(BorderStyle.THIN); //BorderStyle.THIN
        dataCellStyle.setBorderTop(BorderStyle.THIN);//BorderStyle.THIN
        dataCellStyle.setBorderLeft(BorderStyle.THIN);//BorderStyle.THIN
        dataCellStyle.setBorderRight(BorderStyle.THIN);//BorderStyle.THIN

        for(int rowNum = 0; rowNum < studentEntityList.size(); rowNum++) {
            Row row = sheet.createRow(rowNum + 1);

            //setting cell value and cell style
            Cell zeroCell = row.createCell(0);
            zeroCell.setCellValue(studentEntityList.get(rowNum).getId());
            zeroCell.setCellStyle(dataCellStyle);

            Cell firstCell = row.createCell(1);
            firstCell.setCellValue(studentEntityList.get(rowNum).getName());
            firstCell.setCellStyle(dataCellStyle);

            Cell secondCell = row.createCell(2);
            secondCell.setCellValue(studentEntityList.get(rowNum).getContactDetails());
            secondCell.setCellStyle(dataCellStyle);

            Cell thirdCell = row.createCell(3);
            thirdCell.setCellValue(studentEntityList.get(rowNum).getQualification());
            thirdCell.setCellStyle(dataCellStyle);

            Cell fourthCell = row.createCell(4);
            fourthCell.setCellValue(studentEntityList.get(rowNum).getEmail());
            fourthCell.setCellStyle(dataCellStyle);
        }
    }

    private static void createHeaderRow(String[] headers, Sheet sheet, CellStyle headerCellStyle) {

        Row headerRow = sheet.createRow(0);

        for(int index = 0; index < headers.length; index++) {
            Cell headerCell = headerRow.createCell(index);
            headerCell.setCellValue(headers[index]);
            headerCell.setCellStyle(headerCellStyle);
        }
    }

    private static CellStyle getHeaderCellStyle(Workbook workbook) {

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.LIGHT_BLUE.getIndex());

        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setFillForegroundColor((short) 13);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);//FillPatternType.SOLID_FOREGROUND
        headerCellStyle.setBorderBottom(BorderStyle.THIN); //BorderStyle.THIN
        headerCellStyle.setBorderTop(BorderStyle.THIN);//BorderStyle.THIN
        headerCellStyle.setBorderLeft(BorderStyle.THIN);//BorderStyle.THIN
        headerCellStyle.setBorderRight(BorderStyle.THIN);//BorderStyle.THIN

        return headerCellStyle;
    }
}


