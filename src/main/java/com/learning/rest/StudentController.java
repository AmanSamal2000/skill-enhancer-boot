package com.learning.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.learning.model.StudentModel;
import com.learning.service.Impl.StudentService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;


    @GetMapping
    public List<StudentModel> getAllStudent() {
        return studentService.getAllRecords();
    }

    @PostMapping
    public StudentModel save(@RequestBody StudentModel studentModel) {
        return studentService.saveRecord(studentModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        studentService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public StudentModel updateById(@PathVariable Long id, @RequestBody StudentModel studentModel) {
        return studentService.updatedRecordById(id, studentModel);
    }
    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
            studentService.saveExcelFile(file);
            return "file uploaded successfully";
    }

    @GetMapping("/excel-data")
    public List<StudentModel> getDataFromExcel() {
        return studentService.getDataFromExcel();
    }


    //Array postmapping method

//    @GetMapping("get-records")
//    public List<StudentModel> getAllRecords (@RequestParam(value = "count" ,required = false , defaultValue = "0") int count,@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
//        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
//            return studentService.getAllRecords();
//        } else if (count > 0) {
//            return studentService.getLimitedRecords(count);
//        } else {
//            return studentService.getSortedRecords(sortBy);
//        }
//    }
//
//    //Array
//
//    @PostMapping
//    public List<StudentModel> save(@RequestBody List<StudentModel> studentModelList) {
//        try {
//            if (studentModelList.size() == 1) {
//                return Arrays.asList(studentService.saveRecord(studentModelList.get(0)));
//            } else {
//                return studentService.saveAll(studentModelList);
//            }
//        } catch (Exception exception) {
//            System.out.println("Exception Occurs in StudentController || saveAll");
//            System.err.print(exception);
//            return Collections.emptyList();
//        }
//    }
//


}
