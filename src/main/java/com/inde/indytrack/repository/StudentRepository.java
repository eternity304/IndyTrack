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
        value = "WITH student_courses AS ( " +
                "    SELECT DISTINCT scl.course_code " +
                "    FROM course_plans cp " +
                "    JOIN semester_courses sc ON cp.id = sc.course_plan_id " +
                "    JOIN semester_courses_list scl ON sc.id = scl.semester_id " +
                "    WHERE cp.student_id = :studentId " +
                "), " +
                "requirement_credits AS ( " +
                "    SELECT m.name AS minorName, " +
                "           mr.id AS requirement_id, " +
                "           mr.required_credits, " +
                "           COALESCE(MIN(CASE " +
                "               WHEN sc.course_code IS NOT NULL THEN c.credit_value " +
                "               ELSE NULL " +
                "           END), 0.0) AS requirement_credits " +
                "    FROM student_intended_minors sim " +
                "    JOIN minors m ON m.name = sim.minor_name " +
                "    LEFT JOIN minor_requirements mr ON m.name = mr.minor_name " +
                "    LEFT JOIN minor_requirement_courses mrc ON mr.id = mrc.minor_requirement_id " +
                "    LEFT JOIN courses c ON mrc.course_code = c.code " +
                "    LEFT JOIN student_courses sc ON c.code = sc.course_code " +
                "    WHERE sim.student_id = :studentId " +
                "    GROUP BY m.name, mr.id, mr.required_credits " +
                ") " +
                "SELECT minorName, " +
                "       SUM(LEAST(requirement_credits, required_credits)) AS creditsEarned, " +
                "       ROUND(SUM(LEAST(requirement_credits, required_credits)) / 3.0 * 100, 2) AS percentageCompleted " +
                "FROM requirement_credits " +
                "GROUP BY minorName",
        nativeQuery = true
    ) 
    List<MinorProgressDTO> findIntendedMinorsProgress(@Param("studentId") Long studentId);

    @Query(
        value = "WITH student_courses AS ( " +
                "    SELECT DISTINCT scl.course_code " +
                "    FROM course_plans cp " +
                "    JOIN semester_courses sc ON cp.id = sc.course_plan_id " +
                "    JOIN semester_courses_list scl ON sc.id = scl.semester_id " +
                "    WHERE cp.student_id = :studentId " +
                "), " +
                "requirement_credits AS ( " +
                "    SELECT m.name AS minorName, " +
                "           mr.id AS requirement_id, " +
                "           mr.required_credits, " +
                "           COALESCE(MIN(CASE " +
                "               WHEN sc.course_code IS NOT NULL THEN c.credit_value " +
                "               ELSE NULL " +
                "           END), 0.0) AS requirement_credits " +
                "    FROM minors m " +
                "    JOIN minor_requirements mr ON m.name = mr.minor_name " +
                "    LEFT JOIN minor_requirement_courses mrc ON mr.id = mrc.minor_requirement_id " +
                "    LEFT JOIN courses c ON mrc.course_code = c.code " +
                "    LEFT JOIN student_courses sc ON c.code = sc.course_code " +
                "    WHERE m.name = :minorName " +
                "    GROUP BY m.name, mr.id, mr.required_credits " +
                ") " +
                "SELECT minorName, " +
                "       SUM(LEAST(requirement_credits, required_credits)) AS creditsEarned, " +
                "       ROUND(SUM(LEAST(requirement_credits, required_credits)) / 3.0 * 100, 2) AS percentageCompleted " +
                "FROM requirement_credits " +
                "GROUP BY minorName",
        nativeQuery = true
    ) 
    MinorProgressDTO findSpecificMinorProgress(@Param("studentId") Long studentId, @Param("minorName") String minorName);
}
