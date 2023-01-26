package com.learning.mongorest;


import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.learning.model.TimeSlotModel;
import com.learning.mongoservice.Impl.TimeSlotService;


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

}
