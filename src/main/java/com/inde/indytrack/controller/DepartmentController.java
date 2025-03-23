package com.inde.indytrack.controller;

import com.inde.indytrack.exception.DepartmentNotFoundException;
import com.inde.indytrack.model.Department;
import com.inde.indytrack.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private final DepartmentRepository repository;

    public DepartmentController(DepartmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Department> retrieveAllDepartments() {
        return repository.findAll();
    }

    @GetMapping("/{code}")
    public Department retrieveDepartment(@PathVariable("code") String departCode) {
        return repository.findById(departCode)
                .orElseThrow(() -> new DepartmentNotFoundException(departCode));
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        if (repository.existsById(department.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department with code " + department.getCode() + " already exists");
        }
        return repository.save(department);
    }

    @PutMapping("/{code}")
    public Department updateDepartment(@RequestBody Department department, @PathVariable("code") String departmentCode) {
        return repository.findById(departmentCode)
                .map(existingDepartment -> {
                    existingDepartment.setName(department.getName());
                    existingDepartment.setContactEmail(department.getContactEmail());
                    return repository.save(existingDepartment);
                })
                .orElseGet(() -> {
                    department.setCode(departmentCode);
                    department.setName(department.getName());
                    department.setContactEmail(department.getContactEmail());
                    return repository.save(department);
                });
    }

    @DeleteMapping("/{code}")
    public void deleteDepartment(@PathVariable("code") String departmentCode) {
        Department department = repository.findById(departmentCode)
            .orElseThrow(() -> new DepartmentNotFoundException(departmentCode));
        repository.delete(department);
    }

}