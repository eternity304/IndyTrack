package com.inde.indytrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inde.indytrack.model.Review;
import com.inde.indytrack.model.ReviewKey;

public interface ReviewRepository extends JpaRepository<Review, ReviewKey> {
    List<Review> findByCourseCode(String courseCode);
    List<Review> findByStudentId(Long studentId);

    @Query(
        value = "SELECT * FROM reviews r " + 
                "WHERE r.course_code = :courseCode " + 
                "ORDER BY r.upload_time DESC", 
        nativeQuery = true
    )
    List<Review> findLatestReviewsByCourseCode(@Param("courseCode") String courseCode);
}
