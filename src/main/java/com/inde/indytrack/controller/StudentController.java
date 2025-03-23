package com.inde.indytrack.controller;

import com.inde.indytrack.dto.RegisterDTO;
import com.inde.indytrack.dto.StudentDTO;
import com.inde.indytrack.dto.MinorProgressDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    @GetMapping("/{id}/intended-minors")
    public List<MinorProgressDTO> retrieveIntendedMinors(@PathVariable("id") Long studentId) {
        Student student = repository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));

        List<String> intendedMinorNames = student.getIntendedMinors().stream().map(Minor::getName).collect(Collectors.toList());
        
        List<MinorProgressDTO> intendedMinorsProgress = new ArrayList<>();

        for (String minorName : intendedMinorNames) {
            intendedMinorsProgress.addAll(repository.findIntendedMinorsProgress(studentId, minorName));
        }

        return intendedMinorsProgress;
    }

    @PostMapping("/{id}/intended-minors/{minorName}")
    public void createIntendedMinor(@PathVariable("id") Long studentId, @PathVariable("minorName") String minorName) {
        Student student = repository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
        Minor minor = minorRepository.findById(minorName).orElseThrow(() -> new MinorNotFoundException(minorName));

        if (!student.getIntendedMinors().contains(minor)) {
            student.getIntendedMinors().add(minor);
            repository.save(student);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minor is already in the student's intended minors");
        }
    }

    @DeleteMapping("/{id}/intended-minors/{minorName}")
    public void deleteIntendedMinor(@PathVariable("id") Long studentId, @PathVariable("minorName") String minorName) {
        Student student = repository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));
        Minor minor = minorRepository.findById(minorName).orElseThrow(() -> new MinorNotFoundException(minorName));

        if (student.getIntendedMinors().contains(minor)) {
            student.getIntendedMinors().remove(minor);
            repository.save(student);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The " + minorName + " minor is not in the student's intended minors");
        }
    }

    @GetMapping("/{id}/suggested-minors")
    public List<MinorProgressDTO> retrieveSuggestedMinors(@PathVariable("id") Long studentId) {
        Student student = repository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(studentId));

        List<MinorProgressDTO> suggestedMinorsProgress = repository.findSuggestedMinorsProgress(studentId);

        Set<String> intendedMinorNames = student.getIntendedMinors().stream().map(Minor::getName).collect(Collectors.toSet());

        suggestedMinorsProgress.removeIf(suggestedMinor -> intendedMinorNames.contains(suggestedMinor.getMinorName()));

        return suggestedMinorsProgress;
    }
}
