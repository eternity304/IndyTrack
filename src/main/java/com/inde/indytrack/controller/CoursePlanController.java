package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CoursePlanDTO;
import com.inde.indytrack.entity.Course;
import com.inde.indytrack.entity.CoursePlan;
import com.inde.indytrack.entity.Student;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.entity.SemesterCourses;
import com.inde.indytrack.repository.CoursePlanRepository;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@CrossOrigin
@RestController
@RequestMapping("/coursePlans")
public class CoursePlanController {

    @Autowired
    private CoursePlanRepository coursePlanRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    @PostMapping
    public CoursePlan createCoursePlan(@RequestBody CoursePlanDTO coursePlanDTO) {
        Student student = studentRepository.findById(coursePlanDTO.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        CoursePlan coursePlan = new CoursePlan(student, coursePlanDTO.getSemesterCourses());
        return coursePlanRepository.save(coursePlan);
    }

    @DeleteMapping("/{planId}")
    public String deleteCoursePlan(@PathVariable Long planId) {
        coursePlanRepository.deleteById(planId);
        return "Course plan deleted successfully";
    }

    @PutMapping("/{planId}/semester/{semester}/add_course/{courseId}")
    public CoursePlan addCourse(
        @PathVariable Long planId,
        @PathVariable String semester,
        @PathVariable String courseId
    ) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
            .orElseThrow(() -> new RuntimeException("Course Plan not found"));
        
        Optional<SemesterCourses> optionalSemesterCourses = coursePlan.getSemesterCoursesList().stream()
            .filter(sc -> sc.getSemester().toLowerCase().equals(semester.toLowerCase()))
            .findFirst();
        
        SemesterCourses semesterCourses;
        // update if semester course is present else create the corresponding semester so that
        // the course can be added in
        if (optionalSemesterCourses.isPresent()) {
            semesterCourses = optionalSemesterCourses.get();
        } else {
            semesterCourses = new SemesterCourses(semester, new ArrayList<>(), coursePlan);
            coursePlan.getSemesterCoursesList().add(semesterCourses);
        }

        // Add the course if it is not already in the list
        if (!semesterCourses.getCourses().contains(courseId)) {
            semesterCourses.getCourses().add(courseId);
        }

        return coursePlanRepository.save(coursePlan);
    }

    // @PutMapping("/{planId}/remove-course/{courseCode}")
    // public CoursePlan removeCourse(@PathVariable Long planId, @PathVariable String courseCode) {
    //     CoursePlan coursePlan = coursePlanRepository.findById(planId)
    //             .orElseThrow(() -> new RuntimeException("Course Plan not found"));

    //     Course course = courseRepository.findById(courseCode)
    //             .orElseThrow(() -> new RuntimeException("Course not found"));

    //     coursePlan.getCourseSemesterMap().remove(courseCode);
    //     return coursePlanRepository.save(coursePlan);
    // }

    


}