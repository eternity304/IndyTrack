package com.inde.indytrack.controller;

import com.inde.indytrack.exception.DepartmentNotFoundException;
import com.inde.indytrack.entity.Department;
import com.inde.indytrack.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class DepartmentController {
    @Autowired
    private final DepartmentRepository repository;

    public DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/departments")
    List<Department> retrieveAllDepartments() {
        return repository.findAll();
    }

    @GetMapping("/departments/{code}")
    Department retrieveDepartment(@PathVariable("code") String departCode) {
        return repository.findById(departCode)
                .orElseThrow(() -> new DepartmentNotFoundException(departCode));
    }

}