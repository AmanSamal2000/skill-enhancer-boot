package com.learning.mongo.mongocontroller;

import com.learning.model.CourseModel;
import com.learning.mongo.mongoservice.impl.CourseMongoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/course-mongo")
@RequiredArgsConstructor
public class CourseMongoController {

    private final CourseMongoService courseMongoService;


    @GetMapping
    public List<CourseModel> getAllCourse() {
        return courseMongoService.getAllRecords();
    }

    @PostMapping
    public CourseModel save(@RequestBody CourseModel courseModel) {
        return courseMongoService.saveRecord(courseModel);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        courseMongoService.deleteRecordById(id);
    }

    @PutMapping("/{id}")
    public CourseModel updateById(@PathVariable Long id, @RequestBody CourseModel courseModel) {
        return courseMongoService.updatedRecordById(id, courseModel);
    }
}
