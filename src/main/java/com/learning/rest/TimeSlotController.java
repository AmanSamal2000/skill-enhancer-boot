package com.learning.rest;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.model.StudentModel;
import com.learning.model.TimeSlotModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import com.learning.model.TimeSlotModel;
import com.learning.service.Impl.TimeSlotService;


@RestController
@RequestMapping("/timeSlot")
@RequiredArgsConstructor
public class TimeSlotController {

    private final TimeSlotService timeSlotService;


    @GetMapping
    public List<TimeSlotModel> getAllTimeSlot() {
        return timeSlotService.getAllRecords();
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


