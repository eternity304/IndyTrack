package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inde.indytrack.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    
}
