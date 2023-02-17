package com.learning.rest;


import java.util.*;

import com.learning.model.StudentBatchModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.learning.service.mysql.StudentBatchMysqlService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/studentBatch")
@RequiredArgsConstructor
public class StudentBatchController {

    private final StudentBatchMysqlService studentBatchService;


    @GetMapping
    public List<StudentBatchModel> getAllStudentBatch() {
        return studentBatchService.getAllRecords();
    }

    @GetMapping("/limited-records")
    public List<StudentBatchModel> getLimitedRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count){
        return studentBatchService.getLimitedRecords(count);
    }

    @GetMapping("/sorted-records")
    public List<StudentBatchModel> getSortedRecords(@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy){
        return studentBatchService.getSortedRecords(sortBy);
    }

    @GetMapping("/recordBy-id")
    public Optional<StudentBatchModel> getRecordById(Long id){
        return studentBatchService.getRecordById(id);
    }

    @PostMapping("/save-all")
    public List<StudentBatchModel> saveAll(List<StudentBatchModel> studentBatchModelList){
        return studentBatchService.saveAll(studentBatchModelList);
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

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        studentBatchService.saveExcelFile(file);
        return "file uploaded successfully";
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
