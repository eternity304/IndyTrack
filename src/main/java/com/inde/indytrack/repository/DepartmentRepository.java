package com.inde.indytrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inde.indytrack.entity.Department;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
    
}
