package com.learning.rest;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.model.BatchModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    //Array postmapping method

  /*  @GetMapping("get-records")
    public List<BatchModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return batchService.getAllRecords();
        } else if (count > 0) {
            return batchService.getLimitedRecords(count);
        } else {
            return batchService.getSortedRecords(sortBy);
        }
    }


    @PostMapping
    public List<BatchModel> save(@RequestBody List<BatchModel> batchModelList) {
        try {
            if (batchModelList.size() == 1) {
                return Arrays.asList(batchService.saveRecord(batchModelList.get(0)));
            } else {
                return batchService.saveAll(batchModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }*/


}
