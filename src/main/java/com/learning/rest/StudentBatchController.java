package com.learning.rest;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.learning.model.StudentBatchModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.learning.model.StudentBatchModel;
import com.learning.service.Impl.StudentBatchService;


@RestController
@RequestMapping("/studentBatch")
@RequiredArgsConstructor
public class StudentBatchController {

    private final StudentBatchService studentBatchService;


    @GetMapping
    public List<StudentBatchModel> getAllStudentBatch() {
        return studentBatchService.getAllRecords();
    }

    @PostMapping
    public StudentBatchModel save(@RequestBody StudentBatchModel studentBatchModel) {
        return studentBatchService.saveRecord(studentBatchModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        studentBatchService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public StudentBatchModel updateById(@PathVariable Long id, @RequestBody StudentBatchModel studentBatchModel) {
        return studentBatchService.updatedRecordById(id, studentBatchModel);
    }

    //Array postmapping method
    /*

    @GetMapping("get-records")
    public List<StudentBatchModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return studentBatchService.getAllRecords();
        } else if (count > 0) {
            return studentBatchService.getLimitedRecords(count);
        } else {
            return studentBatchService.getSortedRecords(sortBy);
        }
    }



    @PostMapping
    public List<StudentBatchModel> save(@RequestBody List<StudentBatchModel> studentBatchModelList) {
        try {
            if (studentBatchModelList.size() == 1) {
                return Arrays.asList(studentBatchService.saveRecord(studentBatchModelList.get(0)));
            } else {
                return studentBatchService.saveAll(studentBatchModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }*/


}
