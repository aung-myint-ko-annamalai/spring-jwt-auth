package com.ucsy.springjwtauth.service;

import com.ucsy.springjwtauth.exception.NotFoundException;
import com.ucsy.springjwtauth.model.Course;
import com.ucsy.springjwtauth.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    CourseRepository courseRepository;

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> findCourseById(int id) {
        return Optional.ofNullable(courseRepository.findById(id).
                orElseThrow(() -> new NotFoundException("Course not found")));
    }

    public void createCourse(Course course) {
        courseRepository.save(course);
    }


}
