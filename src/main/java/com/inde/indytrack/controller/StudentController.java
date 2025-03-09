package com.inde.indytrack.controller;

import com.inde.indytrack.dto.RegisterDTO;
import com.inde.indytrack.dto.StudentDTO;
import com.inde.indytrack.entity.Student;
import com.inde.indytrack.exception.StudentNotFoundException;
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
    /*@Autowired
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

    @PostMapping
    StudentDTO createStudent(@RequestBody RegisterDTO newStudent) {
        Student student = new Student();
        student.setFirstName(newStudent.getFirstName());
        student.setLastName(newStudent.getLastName());
        student.setEmail(newStudent.getEmail());
        student.setPassword(newStudent.getPassword());

        Student savedStudent = repository.save(student);

        StudentDTO response = new StudentDTO();
        response.setId(savedStudent.getId());
        response.setFirstName(savedStudent.getFirstName());
        response.setLastName(savedStudent.getLastName());
        response.setEmail(savedStudent.getEmail());
        response.setPassword(savedStudent.getPassword());

        return response;
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
    }*/

    @Autowired
    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    List<StudentDTO> retrieveAllStudents() {
        return repository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    StudentDTO retrieveStudent(@PathVariable("id") Long studentId) {
        Student student = repository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        return convertToDTO(student);
    }

    @PostMapping
    StudentDTO createStudent(@RequestBody RegisterDTO newStudent) {
        Student student = new Student();
        student.setFirstName(newStudent.getFirstName());
        student.setLastName(newStudent.getLastName());
        student.setEmail(newStudent.getEmail());
        student.setPassword(newStudent.getPassword());

        Student savedStudent = repository.save(student);
        return convertToDTO(savedStudent);
    }

    @PutMapping("/{id}")
    StudentDTO updateStudent(@RequestBody StudentDTO updatedStudent, @PathVariable("id") Long studentId) {
        return repository.findById(studentId)
                .map(student -> {
                    student.setFirstName(updatedStudent.getFirstName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setPassword(updatedStudent.getPassword());
                    Student savedStudent = repository.save(student);
                    return convertToDTO(savedStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
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
    List<StudentDTO> retrieveStudentByName(@PathVariable("searchstring") String searchString) {
        return repository.findByFirstOrLastName(searchString)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private StudentDTO convertToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPassword(student.getPassword());
        return dto;
    }
}
