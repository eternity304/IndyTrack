package com.inde.indytrack.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.inde.indytrack.dto.MinorDTO;
import com.inde.indytrack.dto.MinorRequirementDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.MinorNotFoundException;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.Minor;
import com.inde.indytrack.model.MinorRequirement;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.MinorRepository;

@RestController
@RequestMapping("/minors")
public class MinorController {
    @Autowired
    private MinorRepository minorRepository;

    @Autowired
    private CourseRepository courseRepository;

    public MinorController(MinorRepository minorRepository, CourseRepository courseRepository) {
        this.minorRepository = minorRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Minor> retrieveAllMinors() {
        return minorRepository.findAll();
    }

    @GetMapping("/{minorName}")
    public Minor retrieveMinor(@PathVariable("minorName") String minorName) {
        Minor minor = minorRepository.findByName(minorName);
        if (minor == null) {
            throw new MinorNotFoundException(minorName);
        }
        return minor;
    }

    private void validateMinorRequirements(Set<MinorRequirementDTO> requirements) {
        double totalCredits = requirements.stream()
            .mapToDouble(MinorRequirementDTO::getRequiredCredits)
            .sum();
        if (totalCredits != 3.0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,"Total credits must be 3.0 but currently is " + totalCredits
            );
        }
    }

    private void updateMinorWithDTO(Minor minor, MinorDTO minorDto) {
        minor.setName(minorDto.getName());
        
        if (minorDto.getRequirements() != null) {
            validateMinorRequirements(minorDto.getRequirements());
            
            // Clear existing requirements
            minor.getRequirements().clear();
            
            // Add new requirements
            for (MinorRequirementDTO reqDto : minorDto.getRequirements()) {
                MinorRequirement requirement = new MinorRequirement();
                requirement.setMinor(minor);
                requirement.setRequiredCredits(reqDto.getRequiredCredits());
                
                Set<Course> courses = new HashSet<>();
                for (String courseCode : reqDto.getCourseCodes()) {
                    Course course = courseRepository.findById(courseCode)
                        .orElseThrow(() -> new CourseNotFoundException(courseCode));
                    courses.add(course);
                }
                requirement.setCourses(courses);
                
                minor.getRequirements().add(requirement);
            }
        }
    }

    @PostMapping
    public Minor createMinor(@RequestBody MinorDTO minorDto) {
        if (minorRepository.findByName(minorDto.getName()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The " + minorDto.getName() + " minor already exists");
        }
        Minor minor = new Minor();
        updateMinorWithDTO(minor, minorDto);
        return minorRepository.save(minor);
    }

    @PutMapping("/{minorName}")
    public Minor updateMinor(@PathVariable("minorName") String minorName, @RequestBody MinorDTO minorDto) {
        Minor minor = minorRepository.findByName(minorName);
        if (minor == null) {
            throw new MinorNotFoundException(minorName);
        }

        // If name is being changed, check if new name already exists
        if (!minorName.equals(minorDto.getName()) && 
            minorRepository.findByName(minorDto.getName()) != null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Minor with name " + minorDto.getName() + " already exists"
            );
        }

        updateMinorWithDTO(minor, minorDto);
        return minorRepository.save(minor);
    }

    @DeleteMapping("/{minorName}")
    public void deleteMinor(@PathVariable("minorName") String minorName) {
        Minor minor = minorRepository.findByName(minorName);
        if (minor == null) {
            throw new MinorNotFoundException(minorName);
        }
        try {
            minorRepository.deleteStudentIntendedMinors(minorName);
            minorRepository.deleteRequirementsByMinorName(minorName);
            minorRepository.delete(minor);
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting minor: " + e.getMessage()
            );
        }
    }

    // Requirement-specific operations
    @GetMapping("/{minorName}/requirements")
    public Set<MinorRequirement> getMinorRequirements(@PathVariable("minorName") String minorName) {
        Minor minor = minorRepository.findByName(minorName);
        if (minor == null) {
            throw new MinorNotFoundException(minorName);
        }
        return minor.getRequirements();
    }
}
