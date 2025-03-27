package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.model.Rating;
import com.inde.indytrack.model.RatingKey;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingKey> {
    List<Rating> findByCourseCode(String courseCode);
    List<Rating> findByStudentId(Long studentId);
}
