package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.entity.CoursePlan;
import java.util.List;

@Repository
public interface CoursePlanRepository extends JpaRepository<CoursePlan, Long> {
    List<CoursePlan> findByStudentId(Long studentId);
}
