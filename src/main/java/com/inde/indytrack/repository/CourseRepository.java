package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.model.AcademicFocus;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.CourseType;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @Query(
        value = "SELECT * FROM courses c " + 
                "WHERE LOWER(c.code) LIKE LOWER(CONCAT(:code, '%'))",
                nativeQuery = true
    ) public Course findByCode(@Param("code") String code);

    public List<Course> findByName(String name);
    
    @Query(
        value = "SELECT * FROM courses c " + 
                "JOIN course_minors cm ON c.code = cm.course_code " + 
                "WHERE cm.minor_name = :minor",
        nativeQuery = true
    ) public List<Course> findByMinor(@Param("minor") String minor);

    public List<Course> findByCourseType(@Param("courseType") CourseType courseType);

    public List<Course> findByCreditValue(@Param("creditValue") Long creditValue);

    public List<Course> findByAcademicFocus(@Param("academicFocus") AcademicFocus academicFocus);

    @Query(
        value = "SELECT * FROM courses c " + 
                "WHERE SUBSTRING(c.code, POSITION(SUBSTRING(c.code FROM '[0-9]') IN c.code), 1) = CAST(:level/100 AS VARCHAR)",
                nativeQuery = true
    ) public List<Course> findByLevel(@Param("level") Integer level);
}
