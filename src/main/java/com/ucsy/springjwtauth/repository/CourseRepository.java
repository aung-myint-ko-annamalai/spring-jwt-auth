package com.ucsy.springjwtauth.repository;

import com.ucsy.springjwtauth.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
}
