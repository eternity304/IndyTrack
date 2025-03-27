package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CourseDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/courses")
public class CourseController {
    @Autowired
    private final CourseRepository repository;

    public CourseController(CourseRepository repository) {
        this.repository = repository;
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

    // Helper function to update the course with the given DTO
    private void updateCourseWithDTO(Course course, CourseDTO dto) {

        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
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
