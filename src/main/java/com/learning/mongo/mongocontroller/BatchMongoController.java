package com.learning.mongo.mongocontroller;

import com.learning.model.BatchModel;
import com.learning.mongo.mongoservice.impl.BatchMongoService;
import com.learning.service.Impl.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch-mongo")
@RequiredArgsConstructor
public class BatchMongoController {

    private final BatchMongoService batchMongoService;


    @GetMapping
    public List<BatchModel> getAllBatch() {
        return batchMongoService.getAllRecords();
    }

    @PostMapping
    public BatchModel save(@RequestBody BatchModel batchModel) {
        return batchMongoService.saveRecord(batchModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        batchMongoService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public BatchModel updateById(@PathVariable Long id, @RequestBody BatchModel batchModel) {
        return batchMongoService.updatedRecordById(id, batchModel);
    }
}
