package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CourseDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.MinorNotFoundException;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.Minor;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.MinorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private final CourseRepository repository;

    @Autowired
    private final MinorRepository minorRepository;

    public CourseController(CourseRepository repository, MinorRepository minorRepository) {
        this.repository = repository;
        this.minorRepository = minorRepository;
    }

    @GetMapping
    public List<Course> retrieveAllCourses() {
        return repository.findAll();
    }

    @GetMapping("/{code}")
    public Course retrieveCourse(@PathVariable("code") String courseCode) {
        return repository.findById(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
    }

    // Helper function for collecting prerequisite courses from their codes
    private Set<Course> getPrerequisitesFromCodes(Set<String> prerequisiteCourseCodes) {
        Set<Course> prerequisites = new HashSet<>();

        for (String code : prerequisiteCourseCodes) {
            Course course = repository.findByCode(code);
            if (course == null) {
                throw new CourseNotFoundException(code);
            }
            prerequisites.add(course);
        }
        return prerequisites;
    }

    // Helper function for collecting minor courses from their names
    private Set<Minor> getMinorsFromNames(Set<String> minorNames) {
        Set<Minor> minors = new HashSet<>();

        for (String name : minorNames) {
            Minor minor = minorRepository.findByName(name);
            if (minor == null) {
                throw new MinorNotFoundException(name);
            }
            minors.add(minor);
        }
        return minors;
    }

    // Helper function to update the course with the given DTO
    private void updateCourseWithDTO(Course course, CourseDTO dto) {
        Set<Course> prerequisites = getPrerequisitesFromCodes(dto.getPrerequisiteCourseCodes());
        Set<Minor> minors = getMinorsFromNames(dto.getMinorNames());

        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setPrerequisites(prerequisites);
        course.setMinors(minors);
        course.setCourseType(dto.getCourseType());
        course.setCreditValue(dto.getCreditValue());
        course.setAcademicFocus(dto.getAcademicFocus());
    }

    @PostMapping
    public Course createCourse(@RequestBody CourseDTO courseDto) {
        Course newCourse = new Course();
        updateCourseWithDTO(newCourse, courseDto);
        return repository.save(newCourse);
    }

    @PutMapping("/{code}")
    public Course updateCourse(@RequestBody CourseDTO courseDto, @PathVariable("code") String courseCode) {
        return repository.findById(courseCode)
                .map(course -> {
                    updateCourseWithDTO(course, courseDto);
                    return repository.save(course);
                })
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
    }

    @DeleteMapping("/{code}")
    public void deleteCourse(@PathVariable("code") String courseCode) {
        Course course = repository.findById(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
        repository.delete(course);
    }

    @GetMapping("/level/{level}")
    public List<Course> getCoursesByLevel(@PathVariable("level") Integer level) {
        List<Course> courses = repository.findByLevel(level);
        if (courses.isEmpty()) {
            throw new CourseNotFoundException(level);
        }
        return courses;
    }
}
