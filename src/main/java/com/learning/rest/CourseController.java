package com.learning.mongorest;

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

import com.learning.model.CourseModel;
import com.learning.mongoservice.Impl.CourseService;


@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;


    @GetMapping
    public List<CourseModel> getAllCourse() {
        return courseService.getAllRecords();
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
}
