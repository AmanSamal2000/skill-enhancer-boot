package com.learning.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import com.learning.model.TrainerModel;
import com.learning.service.Impl.TrainerService;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;


    @GetMapping
    public List<TrainerModel> getAllTrainer() {
        return trainerService.getAllRecords();
    }

    @PostMapping
    public TrainerModel save(@RequestBody TrainerModel trainerModel) {
        return trainerService.saveRecord(trainerModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        trainerService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public TrainerModel updateById(@PathVariable Long id, @RequestBody TrainerModel trainerModel) {
        return trainerService.updatedRecordById(id, trainerModel);
    }

//Array postmapping method

 /*   @GetMapping("get-records")
    public List<TrainerModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return trainerService.getAllRecords();
        } else if (count > 0) {
            return trainerService.getLimitedRecords(count);
        } else {
            return trainerService.getSortedRecords(sortBy);
        }
    }

    @PostMapping
    public List<TrainerModel> save(@RequestBody List<TrainerModel> trainerModelList) {
        try {
            if (trainerModelList.size() == 1) {
                return Arrays.asList(trainerService.saveRecord(trainerModelList.get(0)));
            } else {
                return trainerService.saveAll(trainerModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }*/


}
