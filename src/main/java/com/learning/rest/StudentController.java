package com.learning.rest;

import java.util.*;

import com.learning.service.StudentService;
import com.learning.utility.email.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.learning.model.StudentModel;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

     @Autowired
     private EmailSender emailSender;

    @GetMapping
    public List<StudentModel> getAllRecords() {
        return studentService.getAllRecords();
    }

    @GetMapping("/limited-records")
    public List<StudentModel> getLimitedRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count){
        return studentService.getLimitedRecords(count);
    }

    @GetMapping("/sorted-records")
    public List<StudentModel> getSortedRecords(@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy){
        return studentService.getSortedRecords(sortBy);
    }

    @GetMapping("/recordBy-id")
    public StudentModel getRecordById(Long id){
        return studentService.getRecordById(id);
    }

    @PostMapping
    public StudentModel save(@RequestBody StudentModel studentModel) {
        return studentService.saveRecord(studentModel);
    }

    @PostMapping("/save-all")
    public List<StudentModel> saveAll(List<StudentModel> studentModelList){
        return studentService.saveAll(studentModelList);
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

    @PostMapping("/email")
    public void emailSender(){
        studentService.emailSender();
    }
    @PostMapping("/email/attachment")
    public void sentEmailWithAttachment() {
        studentService.sentEmailWithAttachment();
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
