package com.learning.rest;


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

import com.learning.model.BatchModel;
import com.learning.service.Impl.BatchService;


@RestController
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;


    @GetMapping
    public List<BatchModel> getAllBatch() {
        return batchService.getAllRecords();
    }

    @PostMapping
    public BatchModel save(@RequestBody BatchModel batchModel) {
        return batchService.saveRecord(batchModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        batchService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public BatchModel updateById(@PathVariable Long id, @RequestBody BatchModel batchModel) {
        return batchService.updatedRecordById(id, batchModel);
    }
}
