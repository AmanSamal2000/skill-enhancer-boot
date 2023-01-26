package com.learning.mongo.mongocontroller;

import com.learning.model.TimeSlotModel;
import com.learning.mongo.mongoservice.impl.TimeSlotMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/timeSlot-mongo")
@RequiredArgsConstructor
public class TimeSlotMongoController {

    private final TimeSlotMongoService timeSlotMongoService;


    @GetMapping
    public List<TimeSlotModel> getAllTimeSlot() {
        return timeSlotMongoService.getAllRecords();
    }

    @PostMapping
    public TimeSlotModel save(@RequestBody TimeSlotModel timeSlotModel) {
        return timeSlotMongoService.saveRecord(timeSlotModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        timeSlotMongoService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public TimeSlotModel updateById(@PathVariable Long id, @RequestBody TimeSlotModel timeSlotModel) {
        return timeSlotMongoService.updatedRecordById(id, timeSlotModel);
    }

}
