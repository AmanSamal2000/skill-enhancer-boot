package com.learning.utility.excel;


import com.learning.entity.mysql.TimeSlotEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class TimeSlotReader {

    public List<TimeSlotEntity> getTimeSlotObjects(InputStream inputStream) {

        List<TimeSlotEntity> timeSlotEntityList = new ArrayList<>();
        try {

            
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            
            XSSFSheet sheet = workbook.getSheetAt(0);

            getTimeSlotList(sheet, timeSlotEntityList);

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeSlotEntityList;
    }

    private static void getTimeSlotList(XSSFSheet sheet, List<TimeSlotEntity> timeSlotEntityList) {
        for (int index = sheet.getFirstRowNum() + 1; index <= sheet.getLastRowNum(); index++) {
            Row row = sheet.getRow(index);

            TimeSlotEntity timeSlotEntity = new TimeSlotEntity();

            for (int index2 = row.getFirstCellNum(); index2 < row.getLastCellNum(); index2++) {
                Cell cell = row.getCell(index2);
                if (index2 == 0) {
                    timeSlotEntity.setId((long) cell.getNumericCellValue());
                } else if (index2 == 1) {
                    timeSlotEntity.setStartTime(cell.getLocalDateTimeCellValue().toLocalTime());
                } else if (index2 == 2) {
                    timeSlotEntity.setEndTime(cell.getLocalDateTimeCellValue().toLocalTime());
                } else if (index2 == 3) {
                    timeSlotEntity.setTrainerId((long)cell.getNumericCellValue());
                } else {
                    System.err.println("data not found");
                }
            }

            timeSlotEntityList.add(timeSlotEntity);
        }
    }
}
