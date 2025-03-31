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
    private final StudentRepository repository;

    @Autowired
    private final MinorRepository minorRepository;

    public StudentController(StudentRepository repository, MinorRepository minorRepository) {
        this.repository = repository;
        this.minorRepository = minorRepository;
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
        dto.setIntendedMinorNames(
            student.getIntendedMinors().stream()
                .map(Minor::getName)
                .collect(Collectors.toSet())
        );
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

    // Get intended minors for a student
    @GetMapping("/{studentId}/intended-minors")
    public Set<Minor> getIntendedMinors(@PathVariable Long studentId) {
        Student student = repository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));
        return student.getIntendedMinors();
    }

    // Add an intended minor
    @PutMapping("/{studentId}/intended-minors/{minorName}")
    public StudentDTO addIntendedMinor(
            @PathVariable Long studentId,
            @PathVariable String minorName) {
        Student student = repository.findById(studentId)
            .orElseThrow(() -> new StudentNotFoundException(studentId));
            
        Minor minor = minorRepository.findByName(minorName);
        if (minor == null) {
            throw new MinorNotFoundException(minorName);
        }

        student.getIntendedMinors().add(minor);
        Student savedStudent = repository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    // Remove an intended minor
    @DeleteMapping("/{studentId}/intended-minors/{minorName}")
    public StudentDTO removeIntendedMinor(@PathVariable Long studentId, @PathVariable String minorName) {
        Student student = repository.findById(studentId)
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

        Student savedStudent = repository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    // Update all intended minors at once
    @PutMapping("/{studentId}/intended-minors")
    public StudentDTO updateIntendedMinors(
            @PathVariable Long studentId,
            @RequestBody Set<String> minorNames) {
        Student student = repository.findById(studentId)
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
        Student savedStudent = repository.save(student);
        return convertStudentToDTO(savedStudent);
    }

    // Get progress for all intended minors
    @GetMapping("/{studentId}/intended-minors/progress")
    public List<MinorProgressDTO> getIntendedMinorsProgress(@PathVariable Long studentId) {
        if (!repository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return repository.findSuggestedMinorsProgress(studentId);
    }

    // Get progress for a specific intended minor
    @GetMapping("/{studentId}/intended-minors/{minorName}/progress")
    public MinorProgressDTO getSpecificMinorProgress(
            @PathVariable Long studentId,
            @PathVariable String minorName) {
        if (!repository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        
        MinorProgressDTO progress = repository.findIntendedMinorProgress(studentId, minorName);
        if (progress == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "No progress found for minor: " + minorName
            );
        }
        return progress;
    }
}
