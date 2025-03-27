package com.inde.indytrack.controller;

import com.inde.indytrack.dto.RegisterDTO;
import com.inde.indytrack.dto.StudentDTO;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.model.Student;
import com.inde.indytrack.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private final StudentRepository repository;

    @Autowired
    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<StudentDTO> retrieveAllStudents() {
        return repository.findAll()
                .stream()
                .map(this::convertStudentToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StudentDTO retrieveStudent(@PathVariable("id") Long studentId) {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        return convertStudentToDTO(student);
    }

    @PostMapping
    public StudentDTO createStudent(@RequestBody RegisterDTO newStudent) {
        if (repository.findByEmail(newStudent.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        Student student = new Student();
        student.setFirstName(newStudent.getFirstName());
        student.setLastName(newStudent.getLastName());
        student.setEmail(newStudent.getEmail());
        student.setPassword(newStudent.getPassword());

        Student savedStudent = repository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    private StudentDTO convertStudentToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPassword(student.getPassword());
        return dto;
    }

    @PutMapping("/{id}")
    public StudentDTO updateStudent(@RequestBody StudentDTO updatedStudent, @PathVariable("id") Long studentId) {
        return repository.findById(studentId)
                .map(student -> {
                    student.setFirstName(updatedStudent.getFirstName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setPassword(updatedStudent.getPassword());
                    Student savedStudent = repository.save(student);
                    return convertStudentToDTO(savedStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") Long studentId) {
        if (!repository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        repository.deleteById(studentId);
    }

    @GetMapping("names/{name}")
    public List<StudentDTO> retrieveStudentByName(@PathVariable("name") String name) {
        return repository.findByFirstOrLastName(name).stream().map(this::convertStudentToDTO).collect(Collectors.toList());
    }
}
