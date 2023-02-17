package com.learning.utility.excel;


import com.learning.entity.mysql.TrainerEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class TrainerReader {

    public List<TrainerEntity> getTrainerObjects(InputStream inputStream) {

        List<TrainerEntity> trainerEntityList = new ArrayList<>();
        try {
            
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);

            getTrainerList(sheet, trainerEntityList);

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trainerEntityList;
    }

    private static void getTrainerList(XSSFSheet sheet, List<TrainerEntity> trainerEntityList) {
        for (int index = sheet.getFirstRowNum() + 1; index <= sheet.getLastRowNum(); index++) {
            Row row = sheet.getRow(index);
            TrainerEntity trainerEntity = new TrainerEntity();
            for (int index2 = row.getFirstCellNum(); index2 < row.getLastCellNum(); index2++) {
                Cell cell = row.getCell(index2);
                if (index2 == 0) {
                    trainerEntity.setId((long) cell.getNumericCellValue());
                } else if (index2 == 1) {
                    trainerEntity.setName(cell.getStringCellValue());
                } else if (index2 == 2) {
                    trainerEntity.setSpecialization(cell.getStringCellValue());
                } else {
                    System.err.println("data not found");
                }
            }
            trainerEntityList.add(trainerEntity);
        }
    }
}
