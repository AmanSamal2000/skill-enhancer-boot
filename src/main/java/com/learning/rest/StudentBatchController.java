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
}
