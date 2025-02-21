package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CourseDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.entity.Course;
import com.inde.indytrack.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
public class CourseController {
    @Autowired
    private final CourseRepository repository;

    public CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/courses")
    List<Course> retrieveAllCourses() {
        return repository.findAll();
    }

    @PostMapping("/courses")
    Course createCourse(@RequestBody CourseDTO courseDto) {
        Course newCourse = new Course();
        newCourse.setName(courseDto.getName());
        newCourse.setCode(courseDto.getCode());
        return repository.save(newCourse);
    }

    @GetMapping("/courses/{code}")
    Course retrieveCourse(@PathVariable("code") String courseCode) {
        return repository.findById(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
    }

    @PutMapping("/courses/{code}")
    Course updateCourse(@RequestBody CourseDTO courseDto, @PathVariable("code") String courseCode) {
        return repository.findById(courseCode)
                .map(course -> {
                    course.setName(courseDto.getName());
//                    course.setCode(courseDto.getCode());
                    course.setDescription((courseDto.getDescription()));
                    return repository.save(course);
                })
                .orElseGet(() -> {
                    Course newCourse = new Course();
                    newCourse.setCode(courseCode);
                    newCourse.setName(courseDto.getName());
                    newCourse.setDescription(courseDto.getDescription());
                    return repository.save(newCourse);
                });
    }

    @DeleteMapping("/courses/{code}")
    void deleteCourse(@PathVariable("code") String courseCode) {
        repository.deleteById(courseCode);
    }
}
