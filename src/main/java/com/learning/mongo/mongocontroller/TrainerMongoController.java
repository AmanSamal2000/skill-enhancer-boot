package com.learning.mongo.mongocontroller;

import com.learning.model.TrainerModel;
import com.learning.mongo.mongoservice.impl.TrainerMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trainer-mongo")
@RequiredArgsConstructor
public class TrainerMongoController {

    private final TrainerMongoService trainerMongoService;


    @GetMapping
    public List<TrainerModel> getAllTrainer() {
        return trainerMongoService.getAllRecords();
    }

    @PostMapping
    public TrainerModel save(@RequestBody TrainerModel trainerModel) {
        return trainerMongoService.saveRecord(trainerModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        trainerMongoService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public TrainerModel updateById(@PathVariable Long id, @RequestBody TrainerModel trainerModel) {
        return trainerMongoService.updatedRecordById(id, trainerModel);
    }
}