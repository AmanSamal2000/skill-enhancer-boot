package com.learning.utility.excel;


import com.learning.entity.mysql.CourseEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class CourseReader {

    public List<CourseEntity> getCourseObjects(InputStream inputStream) {

        List<CourseEntity> courseEntityList = new ArrayList<>();
        try {
            
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(2);

            getCourseList(sheet, courseEntityList);

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courseEntityList;
    }

    private static void getCourseList(XSSFSheet sheet, List<CourseEntity> courseEntityList) {
        for (int index = sheet.getFirstRowNum() + 1; index <= sheet.getLastRowNum(); index++) {
            Row row = sheet.getRow(index);

            CourseEntity courseEntity = new CourseEntity();

            for (int index2 = row.getFirstCellNum(); index2 < row.getLastCellNum(); index2++) {
                Cell cell = row.getCell(index2);
                if (index2 == 0) {
                    courseEntity.setId((long) cell.getNumericCellValue());
                } else if (index2 == 1) {
                    courseEntity.setName(cell.getStringCellValue());
                } else if (index2 == 2) {
                    courseEntity.setCurriculum(cell.getStringCellValue());
                } else if (index2 == 3) {
                    courseEntity.setDuration(cell.getStringCellValue());
                } else {
                    System.err.println("data not found");
                }
            }
            courseEntityList.add(courseEntity);
        }
    }
}
