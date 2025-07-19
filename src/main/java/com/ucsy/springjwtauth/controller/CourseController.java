package com.ucsy.springjwtauth.controller;

import com.ucsy.springjwtauth.model.Course;
import com.ucsy.springjwtauth.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody  Course course) {
        courseService.createCourse(course);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created One Course");
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.findAllCourses();
        return ResponseEntity.status(HttpStatus.OK).body(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Course>> getCourseById(@PathVariable int id) {
        Optional<Course> course = courseService.findCourseById(id);
        return ResponseEntity.status(HttpStatus.OK).body(course);
    }

}
