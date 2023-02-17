package com.learning.utility.excel;

import com.learning.entity.mysql.StudentBatchEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class StudentBatchReader {

    public List<StudentBatchEntity> getStudentBatchObjects(InputStream inputStream) {

        List<StudentBatchEntity> studentBatchEntityList = new ArrayList<>();
        try {

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            getStudentBatchList(sheet, studentBatchEntityList);

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentBatchEntityList;
    }

    private static void getStudentBatchList(XSSFSheet sheet, List<StudentBatchEntity> studentBatchEntityList) {
        for (int index = sheet.getFirstRowNum() + 1; index <= sheet.getLastRowNum(); index++) {
            Row row = sheet.getRow(index);

            StudentBatchEntity studentBatchEntity = new StudentBatchEntity();

            for (int index2 = row.getFirstCellNum(); index2 < row.getLastCellNum(); index2++) {
                Cell cell = row.getCell(index2);
                if (index2 == 0) {
                    studentBatchEntity.setId((long) cell.getNumericCellValue());
                } else if (index2 == 1) {
                    studentBatchEntity.setFees(cell.getNumericCellValue());
                } else if (index2 == 2) {
                    studentBatchEntity.setStudentId((long) cell.getNumericCellValue());
                } else if (index2 == 3) {
                    studentBatchEntity.setBatchId((long) cell.getNumericCellValue());
                } else {
                    System.err.println("data not found");
                }
            }
            studentBatchEntityList.add(studentBatchEntity);
        }
    }
}
