package com.example.cms.model.repository;

import com.example.cms.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @Query(value = "select * from courses c " +
            "where lower(c.code) like lower(concat('%', :searchTerm, '%'))", nativeQuery = true)
    List<Course> findCourseById(@Param("searchTerm") String searchTerm);
}
