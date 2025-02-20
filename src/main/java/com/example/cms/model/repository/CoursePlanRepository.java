package com.example.cms.model.repository;

import com.example.cms.model.entity.CoursePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoursePlanRepository extends JpaRepository<CoursePlan, Long> {
    List<CoursePlan> findByStudentId(Long studentId);

}
