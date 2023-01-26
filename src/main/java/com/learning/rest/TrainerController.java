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


import com.learning.model.TrainerModel;
import com.learning.mongoservice.Impl.TrainerService;

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
}
