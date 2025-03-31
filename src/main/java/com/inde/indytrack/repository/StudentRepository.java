package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.dto.MinorProgressDTO;
import com.inde.indytrack.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(
        value = "SELECT * FROM users u " + 
                "WHERE u.user_type = 'student' AND " + 
                "(LOWER(u.first_name) LIKE LOWER(CONCAT('%', :name, '%')) " + 
                "OR LOWER(u.last_name) LIKE LOWER(CONCAT('%', :name, '%')))",
        nativeQuery = true
    )
    List<Student> findByFirstOrLastName(@Param("name") String name);

    Student findByEmail(@Param("email") String email);

    boolean existsByEmail(@Param("email") String email);

    @Query(
        value = "SELECT m.name as minorName, SUM(c.credit_value) as creditsEarned, ROUND(SUM(c.credit_value) / 3.0 * 100, 2) as percentageCompleted " + 
                "FROM courses c " + 
                "INNER JOIN course_minors cm ON c.code = cm.course_code " + 
                "INNER JOIN minors m ON cm.minor_name = m.name " + 
                "INNER JOIN semester_courses_list scl ON c.code = scl.course_code " + 
                "INNER JOIN semester_courses sc ON scl.semester_id = sc.id " + 
                "INNER JOIN course_plans cp ON sc.course_plan_id = cp.id " + 
                "WHERE cp.student_id = :studentId AND LOWER(m.name) LIKE LOWER(CONCAT('%', :minorName, '%'))", 
                nativeQuery = true
    ) 
    MinorProgressDTO findIntendedMinorProgress(@Param("studentId") Long studentId, @Param("minorName") String minorName);

    @Query(
        value = "SELECT m.name as minorName, SUM(c.credit_value) as creditsEarned, ROUND(SUM(c.credit_value) / 3.0 * 100, 2) as percentageCompleted " + 
                "FROM courses c " + 
                "INNER JOIN course_minors cm ON c.code = cm.course_code " +
                "INNER JOIN minors m ON cm.minor_name = m.name " + 
                "INNER JOIN semester_courses_list scl ON c.code = scl.course_code " + 
                "INNER JOIN semester_courses sc ON scl.semester_id = sc.id " +
                "INNER JOIN course_plans cp ON sc.course_plan_id = cp.id " +
                "WHERE cp.student_id = :studentId " +
                "GROUP BY m.name HAVING SUM(c.credit_value) >= 1.5", 
        nativeQuery = true
    ) 
    List<MinorProgressDTO> findSuggestedMinorsProgress(@Param("studentId") Long studentId);
}
