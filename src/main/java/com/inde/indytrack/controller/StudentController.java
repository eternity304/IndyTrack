package com.inde.indytrack.controller;

import com.inde.indytrack.dto.MinorProgressDTO;
import com.inde.indytrack.dto.RegisterDTO;
import com.inde.indytrack.dto.StudentDTO;
import com.inde.indytrack.exception.MinorNotFoundException;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.model.Minor;
import com.inde.indytrack.model.Student;
import com.inde.indytrack.repository.MinorRepository;
import com.inde.indytrack.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final MinorRepository minorRepository;

    public StudentController(StudentRepository studentRepository, MinorRepository minorRepository) {
        this.studentRepository = studentRepository;
        this.minorRepository = minorRepository;
    }

    @GetMapping
    public List<StudentDTO> retrieveAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(this::convertStudentToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public StudentDTO retrieveStudent(@PathVariable("id") Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));
        return convertStudentToDTO(student);
    }

    @PostMapping
    public StudentDTO createStudent(@RequestBody RegisterDTO newStudent) {
        if (studentRepository.findByEmail(newStudent.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use");
        }
        Student student = new Student();
        student.setFirstName(newStudent.getFirstName());
        student.setLastName(newStudent.getLastName());
        student.setEmail(newStudent.getEmail());
        student.setPassword(newStudent.getPassword());
        Student savedStudent = studentRepository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    private StudentDTO convertStudentToDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setEmail(student.getEmail());
        dto.setPassword(student.getPassword());
        dto.setIntendedMinorNames(
            student.getIntendedMinors().stream()
                .map(Minor::getName)
                .collect(Collectors.toSet())
        );
        return dto;
    }

    @PutMapping("/{id}")
    public StudentDTO updateStudent(@RequestBody StudentDTO updatedStudent, @PathVariable("id") Long studentId) {
        return studentRepository.findById(studentId)
                .map(student -> {
                    student.setFirstName(updatedStudent.getFirstName());
                    student.setLastName(updatedStudent.getLastName());
                    student.setEmail(updatedStudent.getEmail());
                    student.setPassword(updatedStudent.getPassword());
                    // Update intended minors if provided
                    if (updatedStudent.getIntendedMinorNames() != null) {
                        Set<Minor> minors = new HashSet<>();
                        for (String minorName : updatedStudent.getIntendedMinorNames()) {
                            Minor minor = minorRepository.findByName(minorName);
                            if (minor == null) {
                                throw new MinorNotFoundException(minorName);
                            }
                            minors.add(minor);
                        }
                    student.setIntendedMinors(minors);
                }
                    Student savedStudent = studentRepository.save(student);
                    return convertStudentToDTO(savedStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(studentId));
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable("id") Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        studentRepository.deleteById(studentId);
    }

    @GetMapping("names/{name}")
    public List<StudentDTO> retrieveStudentByName(@PathVariable("name") String name) {
        return studentRepository.findByFirstOrLastName(name).stream().map(this::convertStudentToDTO).collect(Collectors.toList());
    }

    // Get intended minors for a student
    @GetMapping("/{studentId}/intended-minors")
    public Set<Minor> getIntendedMinors(@PathVariable("studentId") Long studentId) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));
        return student.getIntendedMinors();
    }

    // Add an intended minor
    @PutMapping("/{studentId}/intended-minors/{minorName}")
    public StudentDTO addIntendedMinor(@PathVariable("studentId") Long studentId, @PathVariable("minorName") String minorName) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));
            
        Minor minor = minorRepository.findByName(minorName);
        if (minor == null) {
            throw new MinorNotFoundException(minorName);
        }

        student.getIntendedMinors().add(minor);
        Student savedStudent = studentRepository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    // Remove an intended minor
    @DeleteMapping("/{studentId}/intended-minors/{minorName}")
    public StudentDTO removeIntendedMinor(@PathVariable("studentId") Long studentId, @PathVariable("minorName") String minorName) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));
            
        Minor minor = minorRepository.findByName(minorName);
        if (minor == null) {
            throw new MinorNotFoundException(minorName);
        }

        if (!student.getIntendedMinors().removeIf(m -> m.getName().equals(minorName))) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Student is not enrolled in minor: " + minorName
            );
        }

        Student savedStudent = studentRepository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    // Update all intended minors at once
    @PutMapping("/{studentId}/intended-minors")
    public StudentDTO updateIntendedMinors(@PathVariable("studentId") Long studentId, @RequestBody Set<String> minorNames) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));

        Set<Minor> minors = new HashSet<>();
        for (String minorName : minorNames) {
            Minor minor = minorRepository.findByName(minorName);
            if (minor == null) {
                throw new MinorNotFoundException(minorName);
            }
            minors.add(minor);
        }

        student.setIntendedMinors(minors);
        Student savedStudent = studentRepository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    // Get progress for all intended minors
    @GetMapping("/{studentId}/intended-minors/progress")
    public List<MinorProgressDTO> retrieveAllIntendedMinorsProgress(@PathVariable("studentId") Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return studentRepository.findIntendedMinorsProgress(studentId);
    }

    // Get progress for a specific intended minor
    @GetMapping("/{studentId}/intended-minors/{minorName}/progress")
    public MinorProgressDTO retrieveIntendedMinorProgress(@PathVariable Long studentId, @PathVariable String minorName) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));
        if (!student.getIntendedMinors().stream().anyMatch(m -> m.getName().equals(minorName))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student is not enrolled in the  " + minorName + " minor");
        }
        return studentRepository.findSpecificMinorProgress(studentId, minorName);
    }
}
