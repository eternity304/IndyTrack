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
    private final DepartmentRepository departmentRepository;

    public DepartmentController(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public List<Department> retrieveAllDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping("/{code}")
    public Department retrieveDepartment(@PathVariable("code") String departCode) {
        return departmentRepository.findById(departCode)
                .orElseThrow(() -> new DepartmentNotFoundException(departCode));
    }

    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        if (departmentRepository.existsById(department.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department with code " + department.getCode() + " already exists");
        }
        return departmentRepository.save(department);
    }

    @PutMapping("/{code}")
    public Department updateDepartment(@RequestBody Department department, @PathVariable("code") String departmentCode) {
        return departmentRepository.findById(departmentCode)
                .map(existingDepartment -> {
                    existingDepartment.setName(department.getName());
                    existingDepartment.setContactEmail(department.getContactEmail());
                    return departmentRepository.save(existingDepartment);
                })
                .orElseThrow(() -> new DepartmentNotFoundException(departmentCode));
    }

    @DeleteMapping("/{code}")
    public void deleteDepartment(@PathVariable("code") String departmentCode) {
        Department department = departmentRepository.findById(departmentCode)
            .orElseThrow(() -> new DepartmentNotFoundException(departmentCode));
        departmentRepository.delete(department);
    }

}