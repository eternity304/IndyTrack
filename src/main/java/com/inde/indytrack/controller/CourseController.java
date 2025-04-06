package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CourseDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.model.AcademicFocus;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.CourseType;
import com.inde.indytrack.repository.CourseRepository;

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
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("/{code}")
    public Course retrieveCourse(@PathVariable("code") String courseCode) {
        return courseRepository.findById(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
    }

    // Helper function to update the course with the given DTO
    private void updateCourseWithDTO(Course course, CourseDTO dto) {
        course.setCode(dto.getCode());
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        course.setCourseType(dto.getCourseType());
        course.setCreditValue(dto.getCreditValue());
        course.setAcademicFocus(dto.getAcademicFocus());
        
        // Handle prerequisites
        if (dto.getPrerequisiteCodes() != null) {
            Set<Course> prerequisites = new HashSet<>();
            for (String prereqCode : dto.getPrerequisiteCodes()) {
                Course prerequisite = courseRepository.findById(prereqCode)
                    .orElseThrow(() -> new CourseNotFoundException(prereqCode));
                prerequisites.add(prerequisite);
            }
            course.setPrerequisites(prerequisites);
        }
    }

    @PostMapping
    public Course createCourse(@RequestBody CourseDTO courseDto) {
        Course newCourse = new Course();
        updateCourseWithDTO(newCourse, courseDto);
        return courseRepository.save(newCourse);
    }

    @PutMapping("/{code}")
    public Course updateCourse(@RequestBody CourseDTO courseDto, @PathVariable("code") String courseCode) {
        return courseRepository.findById(courseCode)
                .map(course -> {
                    updateCourseWithDTO(course, courseDto);
                    return courseRepository.save(course);
                })
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
    }

    @DeleteMapping("/{code}")
    public void deleteCourse(@PathVariable("code") String courseCode) {
        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new CourseNotFoundException(courseCode));
        
        course.getIsPrerequisiteFor().forEach(c -> c.getPrerequisites().remove(course));
        course.getPrerequisites().forEach(c -> c.getIsPrerequisiteFor().remove(course));
        course.getMinorRequirements().forEach(mr -> mr.getCourses().remove(course));
        courseRepository.save(course);
        courseRepository.delete(course);
    }

    @GetMapping("/search")
    public List<Course> searchCourses(
        @RequestParam(required = false) String code,
        @RequestParam(required = false) String name,
        @RequestParam(required = false) CourseType courseType,
        @RequestParam(required = false) Long creditValue,
        @RequestParam(required = false) AcademicFocus academicFocus,
        @RequestParam(required = false) Integer level
    ) {
        return courseRepository.searchCourses(
            code,
            name,
            courseType != null ? courseType.name() : null,
            creditValue,
            academicFocus != null ? academicFocus.name() : null,
            level
        );
    }
}
