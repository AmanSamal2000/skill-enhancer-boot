package com.learning.utility.excel;


import com.learning.entity.mysql.BatchEntity;
import com.learning.enums.BatchStatus;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class BatchReader {

    public List<BatchEntity> getBatchObjects(InputStream inputStream) {

        List<BatchEntity> batchEntityList = new ArrayList<>();
        try {

            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0 );

            getBatchList(sheet, batchEntityList);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return batchEntityList;
    }

    private static void getBatchList(XSSFSheet sheet, List<BatchEntity> batchEntityList) {
        for (int index = sheet.getFirstRowNum() + 1; index <= sheet.getLastRowNum(); index++) {
            Row row = sheet.getRow(index);

            BatchEntity batchEntity = new BatchEntity();

            for (int index2 = row.getFirstCellNum(); index2 < row.getLastCellNum(); index2++) {

                Cell cell = row.getCell(index2);
                if (index2 == 0) {
                    batchEntity.setId((long) cell.getNumericCellValue());
                } else if (index2 == 1) {
                    batchEntity.setStudentCount((int) cell.getNumericCellValue());
                } else if (index2 == 2) {
                    batchEntity.setStartDate(cell.getLocalDateTimeCellValue().toLocalDate());
                } else if (index2 == 3) {
                    batchEntity.setEndDate(cell.getLocalDateTimeCellValue().toLocalDate());
                } else if (index2 == 4) {
                    batchEntity.setBatchStatus(BatchStatus.valueOf(cell.getStringCellValue()) );
                }else if(index2 ==5){
                    batchEntity.setCourseId((long) cell.getNumericCellValue());
                }else if (index2 == 6){
                    batchEntity.setTimeSlotId((long) cell.getNumericCellValue());
                }
                else {
                    System.err.println("data not found");
                }
            }

            batchEntityList.add(batchEntity);
        }
    }
}
