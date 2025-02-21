package com.inde.indytrack.controller;

import com.inde.indytrack.entity.Course;
import com.inde.indytrack.entity.CoursePlan;
import com.inde.indytrack.repository.CoursePlanRepository;
import com.inde.indytrack.repository.CourseRepository;
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

    @PutMapping("/{planId}/add-course/{courseCode}/semester/{semester}")
    public CoursePlan addCourse(@PathVariable Long planId, @PathVariable String courseCode, @PathVariable String semester) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Course Plan not found"));

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        coursePlan.updateCourseSemester(courseCode, semester);
        return coursePlanRepository.save(coursePlan);
    }

    @PutMapping("/{planId}/remove-course/{courseCode}")
    public CoursePlan removeCourse(@PathVariable Long planId, @PathVariable String courseCode) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Course Plan not found"));

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        coursePlan.getCourseSemesterMap().remove(courseCode);
        return coursePlanRepository.save(coursePlan);
    }

    @PutMapping("/{planId}/modify-course/{courseCode}/move-to-semester/{semester}")
    public CoursePlan modifyCourse(
            @PathVariable Long planId,
            @PathVariable String courseCode,
            @PathVariable String semester) {

        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Course Plan not found"));

        Course course = courseRepository.findById(courseCode)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        coursePlan.updateCourseSemester(courseCode, semester);

        return coursePlanRepository.save(coursePlan);
    }


    @GetMapping
    public List<CoursePlan> getAllCoursePlans() {
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