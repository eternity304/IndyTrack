package com.example.cms.controller;

import com.example.cms.model.entity.Course;
import com.example.cms.model.entity.CoursePlan;
import com.example.cms.model.repository.CoursePlanRepository;
import com.example.cms.model.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/course_plans")
public class CoursePlanController {

    @Autowired
    private CoursePlanRepository coursePlanRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public CoursePlan createCoursePlan(@RequestBody CoursePlan coursePlan) {
        return coursePlanRepository.save(coursePlan);
    }

    @DeleteMapping("/{planId}")
    public String deleteCoursePlan(@PathVariable Long planId) {
        coursePlanRepository.deleteById(planId);
        return "Course plan deleted successfully";
    }

    @PutMapping("/{planId}/add-course/{courseCode}")
    public CoursePlan addCourse(@PathVariable Long planId, @PathVariable String courseCode) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Course Plan not found"));

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        coursePlan.getCourses().add(course);
        return coursePlanRepository.save(coursePlan);
    }

    @PutMapping("/{planId}/remove-course/{courseCode}")
    public CoursePlan removeCourse(@PathVariable Long planId, @PathVariable String courseCode) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Course Plan not found"));

        coursePlan.getCourses().removeIf(course -> course.getCode().equals(courseCode));
        return coursePlanRepository.save(coursePlan);
    }

    @GetMapping
    public List<CoursePlan> getAllCoursePlans(){
        return coursePlanRepository.findAll();
    }

    @GetMapping("/student/{studentId}")
    public List<CoursePlan> getCoursePlansByStudent(@PathVariable Long studentId) {
        return coursePlanRepository.findByStudentId(studentId);
    }

    @GetMapping("/{planId}")
    public Optional<CoursePlan> getCoursePlanById(@PathVariable Long planId) {
        return coursePlanRepository.findById(planId);
    }

}
