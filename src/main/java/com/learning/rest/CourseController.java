package com.learning.rest;

import java.util.*;

import com.learning.model.CourseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.learning.service.mysql.CourseMysqlService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseMysqlService courseService;


    @GetMapping
    public List<CourseModel> getAllRecords() {
        return courseService.getAllRecords();
    }

    @GetMapping("/limited-records")
    public List<CourseModel> getLimitedRecords(@RequestParam(value = "count" ,required = false , defaultValue = "0") int count){
        return courseService.getLimitedRecords(count);
    }

    @GetMapping("/sorted-records")
    public List<CourseModel> getSortedRecords(@RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy){
        return courseService.getSortedRecords(sortBy);
    }

    @GetMapping("/recordBy-id")
    public Optional<CourseModel> getRecordById(Long id){
        return courseService.getRecordById(id);
    }

    @PostMapping("/save-all")
    public List<CourseModel> saveAll(List<CourseModel> courseModelList){
        return courseService.saveAll(courseModelList);
    }


    @PostMapping
    public CourseModel save(@RequestBody CourseModel courseModel) {
        return courseService.saveRecord(courseModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        courseService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public CourseModel updateById(@PathVariable Long id, @RequestBody CourseModel courseModel) {
        return courseService.updatedRecordById(id, courseModel);
    }

    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file){
        courseService.saveExcelFile(file);
        return "file uploaded successfully";
    }
//Array postmapping method

   /* @GetMapping("get-records")
    public List<CourseModel> getAllRecords(@RequestParam(value = "count", required = false, defaultValue = "0") int count, @RequestParam(value = "sortBy", required = false, defaultValue = "") String sortBy) {
        if (count == 0 && (Objects.isNull(sortBy) || sortBy.isBlank())) {
            return courseService.getAllRecords();
        } else if (count > 0) {
            return courseService.getLimitedRecords(count);
        } else {
            return courseService.getSortedRecords(sortBy);
        }
    }

    //Array

    @PostMapping
    public List<CourseModel> save(@RequestBody List<CourseModel> courseModelList) {
        try {
            if (courseModelList.size() == 1) {
                return Arrays.asList(courseService.saveRecord(courseModelList.get(0)));
            } else {
                return courseService.saveAll(courseModelList);
            }
        } catch (Exception exception) {
            System.out.println("Exception Occurs in StudentController || saveAll");
            System.err.print(exception);
            return Collections.emptyList();
        }
    }*/


}
