package com.learning.mongorest;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.learning.model.StudentModel;
import com.learning.mongoservice.Impl.StudentService;
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

}
