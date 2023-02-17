package com.learning.rest;


import java.util.*;

import com.learning.model.TimeSlotModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import com.learning.service.mysql.TimeSlotMysqlService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/timeSlot")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotMysqlService timeSlotService;


    @GetMapping
    public List<TimeSlotModel> getAllRecords() {
        return timeSlotService.getAllRecords();
    }

    @GetMapping("/limited-records")
    public List<TimeSlotModel> getLimitedRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count){
        return timeSlotService.getLimitedRecords(count);
    }

    @GetMapping("/sorted-records")
    public List<TimeSlotModel> getSortedRecords(@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy){
        return timeSlotService.getSortedRecords(sortBy);
    }

    @GetMapping("/recordBy-id")
    public Optional<TimeSlotModel> getRecordById(Long id){
        return timeSlotService.getRecordById(id);
    }

    @PostMapping("/save-all")
    public List<TimeSlotModel> saveAll(List<TimeSlotModel> timeSlotModelList){
        return timeSlotService.saveAll(timeSlotModelList);
    }


    @PostMapping
    public TimeSlotModel save(@RequestBody TimeSlotModel timeSlotModel) {
        return timeSlotService.saveRecord(timeSlotModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        timeSlotService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public TimeSlotModel updateById(@PathVariable Long id, @RequestBody TimeSlotModel timeSlotModel) {
        return timeSlotService.updatedRecordById(id, timeSlotModel);
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        timeSlotService.saveExcelFile(file);
        return "file uploaded successfully";
    }

    //Array postmapping method

//    @GetMapping("get-records")
//    public List<TimeSlotModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
//        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
//            return timeSlotService.getAllRecords();
//        } else if (count > 0) {
//            return timeSlotService.getLimitedRecords(count);
//        } else {
//            return timeSlotService.getSortedRecords(sortBy);
//        }
//    }
//
//    @PostMapping
//    public List<TimeSlotModel> save(@RequestBody List<TimeSlotModel> timeSlotModelList) {
//        try {
//            if (timeSlotModelList.size() == 1) {
//                return Arrays.asList(timeSlotService.saveRecord(timeSlotModelList.get(0)));
//            } else {
//                return timeSlotService.saveAll(timeSlotModelList);
//            }
//        } catch (Exception exception) {
//            System.out.println("Exception Occurs in StudentController || saveAll");
//            System.err.print(exception);
//            return Collections.emptyList();
//        }
//    }
//
//

}


