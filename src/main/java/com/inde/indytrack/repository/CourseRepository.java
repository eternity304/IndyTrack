package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    
}
