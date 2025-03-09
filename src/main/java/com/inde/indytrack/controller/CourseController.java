package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CourseDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.entity.Course;
import com.inde.indytrack.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    List<Course> retrieveAllCourses() {
        return this.repository.findAll();
    }

    // Helper function for collecting prerequisite
    private Set<Course> getPrerequisitesCourses(Set<String> prerequisistesCodes) {
        if (prerequisistesCodes == null || prerequisistesCodes.isEmpty()) {
            return new HashSet<>();
        } else {
            return prerequisistesCodes.stream() // convert Set<Course> into Stream<Course> which is a way of processing data from sources that allows functional operations
                        .map(this.repository::findById) // it retrieve the course object  from the course repository by searchig its id
                        .filter(Optional::isPresent) // returns optional<course> which is a wrapper class for handling the case in which there is no Course object; this prevent null pointer exceptionn and returns an object
                        .map(Optional::get) // Optional::isPresent filters out empty optional<course> and optional:: get return non-empty ones
                        .collect(Collectors.toSet());
        }
    }

    @PostMapping
    Course createCourse(@RequestBody CourseDTO courseDto) {
        Course newCourse = new Course();
        Set<Course> prerequisites = getPrerequisitesCourses(courseDto.getPrerequisitesCodes());
        
        newCourse.setName(courseDto.getName());
        newCourse.setCode(courseDto.getCode());
        newCourse.setDescription(courseDto.getDescription());
        newCourse.setPrerequisites(prerequisites);
        
        return repository.save(newCourse);
    }

    @GetMapping("/{code}")
    Course retrieveCourse(@PathVariable("code") String courseCode) {
        return repository.findById(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
    }

    @PutMapping("/{code}")
    Course updateCourse(@RequestBody CourseDTO courseDto, @PathVariable("code") String courseCode) {
        return repository.findById(courseCode)
                .map(course -> {
                    course.setName(courseDto.getName());
                    course.setDescription((courseDto.getDescription()));
                    Set<Course> prerequisistes = getPrerequisitesCourses(courseDto.getPrerequisitesCodes());
                    course.setPrerequisites(prerequisistes);
                    return repository.save(course);
                })
                .orElseGet(() -> {
                    Course newCourse = new Course();
                    Set<Course> prerequisites = getPrerequisitesCourses(courseDto.getPrerequisitesCodes());
                    newCourse.setCode(courseCode);
                    newCourse.setName(courseDto.getName());
                    newCourse.setDescription(courseDto.getDescription());
                    newCourse.setPrerequisites(prerequisites);
                    return repository.save(newCourse);
                });
    }

    @DeleteMapping("/{code}")
    String deleteCourse(@PathVariable("code") String courseCode) {
        if (!repository.existsById(courseCode)) {
            throw new CourseNotFoundException(courseCode);
        }
        repository.deleteById(courseCode);
        return "Course " + courseCode + " has been deleted successfully";
    }
}
