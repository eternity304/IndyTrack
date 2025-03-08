package com.inde.indytrack.controller;

import com.inde.indytrack.entity.Student;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<Student> retrieveAllStudents() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Student retrieveStudent(@PathVariable("id") Long studentId) {
        return repository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    @PutMapping("/{id}")
    Student updateStudent(@RequestBody Student newStudent, @PathVariable("id") Long studentId) {
        return repository.findById(studentId)
                .map(student -> {
                    student.setFirstName(newStudent.getFirstName());
                    student.setLastName(newStudent.getLastName());
                    student.setEmail(newStudent.getEmail());
                    student.setPassword(newStudent.getPassword());
                    return repository.save(student);
                })
                .orElseGet(() -> {
                    newStudent.setId(studentId);
                    return repository.save(newStudent);
                });
    }

    @DeleteMapping("/{id}")
    String deleteStudent(@PathVariable("id") Long studentId) {
        if (!repository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        repository.deleteById(studentId);
        return "Student " + studentId + " has been deleted successfully";
    }

    @GetMapping("/search/{searchstring}")
    List<Student> retrieveStudentByName(@PathVariable("searchstring") String searchString) {
        return repository.findByFirstOrLastName(searchString);
    }
}
