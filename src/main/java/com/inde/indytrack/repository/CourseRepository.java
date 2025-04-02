package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.model.AcademicFocus;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.CourseType;

@Repository
public interface CourseRepository extends JpaRepository<Course, String>, JpaSpecificationExecutor<Course> {
    @Query(
        value = "SELECT * FROM courses c " + 
                "WHERE LOWER(c.code) LIKE LOWER(CONCAT(:code, '%'))",
                nativeQuery = true
    )
    Course findByCode(@Param("code") String code);

    List<Course> findByName(String name);

    List<Course> findByCourseType(@Param("courseType") CourseType courseType);

    List<Course> findByCreditValue(@Param("creditValue") Long creditValue);

    List<Course> findByAcademicFocus(@Param("academicFocus") AcademicFocus academicFocus);

    @Query(
        value = "SELECT * FROM courses c " + 
                "WHERE (:level REGEXP_LIKE(c.code, CONCAT('^[A-Z]+', CAST(:level/100 AS VARCHAR))))",
                nativeQuery = true
    )
    List<Course> findByLevel(@Param("level") Integer level);

    @Query(
        value = "SELECT * FROM courses c " +
                "WHERE (:code IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT(:code, '%'))) " +
                "AND (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
                "AND (:courseType IS NULL OR c.course_type = :courseType) " +
                "AND (:creditValue IS NULL OR c.credit_value = :creditValue) " +
                "AND (:academicFocus IS NULL OR EXISTS (SELECT 1 FROM course_academic_focus caf " +
                "     WHERE caf.course_code = c.code AND caf.academic_focus = :academicFocus)) " +
                "AND (:level IS NULL OR REGEXP_LIKE(c.code, CONCAT('^[A-Z]+', CAST(CASE WHEN :level IS NULL THEN NULL ELSE :level/100 END AS VARCHAR))))",
            nativeQuery = true
    )
    List<Course> searchCourses(
        @Param("code") String code,
        @Param("name") String name,
        @Param("courseType") String courseType,
        @Param("creditValue") Long creditValue,
        @Param("academicFocus") String academicFocus,
        @Param("level") Integer level
    );

    @Query(
        value = "SELECT COALESCE(ROUND(AVG(r.rating), 2), 0.0) FROM reviews r " + 
                "WHERE r.course_code = :courseCode", 
        nativeQuery = true
    )
    Double calculateAverageRating(@Param("courseCode") String courseCode);
    
    @Query(
        value = "SELECT COUNT(*) FROM reviews r " + 
                "WHERE r.course_code = :courseCode", 
        nativeQuery = true
    )
    Integer calculateReviewCount(@Param("courseCode") String courseCode);
}
