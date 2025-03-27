package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CoursePlanDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.CoursePlanNotFoundException;
import com.inde.indytrack.exception.InvalidSemesterException;
import com.inde.indytrack.exception.SemesterNotFoundException;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.CoursePlan;
import com.inde.indytrack.model.SemesterCourses;
import com.inde.indytrack.model.Student;
import com.inde.indytrack.repository.CoursePlanRepository;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

@CrossOrigin
@RestController
@RequestMapping("/course-plans")
public class CoursePlanController {

    @Autowired
    private CoursePlanRepository coursePlanRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping
    public List<CoursePlan> retrieveAllCoursePlans() {
        return coursePlanRepository.findAll();
    }

    @GetMapping("/students/{studentId}")
    public List<CoursePlan> retrieveCoursePlansByStudent(@PathVariable Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return coursePlanRepository.findByStudentId(studentId);
    }

    @GetMapping("/{planId}")
    public CoursePlan retrieveCoursePlanById(@PathVariable Long planId) {
        return coursePlanRepository.findById(planId)
            .orElseThrow(() -> new CoursePlanNotFoundException(planId));
    }

    // Helper method to validate the semester format
    private void validateSemester(String semester) {
        String[] parts = semester.split(" ");
        if (parts.length != 2) {
            throw new InvalidSemesterException(semester);
        }
        String term = parts[0].trim().toLowerCase();
        int year;
        try {
            year = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new InvalidSemesterException(semester);
        }
        if (!term.matches("winter|summer|fall")) {
            throw new InvalidSemesterException(semester);
        }
        if (year < 2018 || year > 2030) {
            throw new InvalidSemesterException(semester);
        } 
    }

    @PostMapping
    public CoursePlan createCoursePlan(@RequestBody CoursePlanDTO coursePlanDto) {
        Student student = studentRepository.findById(coursePlanDto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException(coursePlanDto.getStudentId()));

        for (String semester : coursePlanDto.getSemesterCourses().keySet()) {
            validateSemester(semester);
        }

        CoursePlan coursePlan = new CoursePlan(student, coursePlanDto.getSemesterCourses());
        return coursePlanRepository.save(coursePlan);
    }

    @DeleteMapping("/{planId}")
    public void deleteCoursePlan(@PathVariable Long planId) {
        if (!coursePlanRepository.existsById(planId)) {
            throw new CoursePlanNotFoundException(planId);
        }
        coursePlanRepository.deleteById(planId);
    }

    // Helper method to find semester courses in a course plan
    private Optional<SemesterCourses> findSemesterCourses(CoursePlan coursePlan, String semester) {
        return coursePlan.getSemesterCoursesList().stream()
                .filter(sc -> sc.getSemester().equals(semester))
                .findFirst();
    }

    @PutMapping("/{planId}/{semester}")
    public CoursePlan addSemester(@PathVariable Long planId, @PathVariable String semester) {
        validateSemester(semester);

        CoursePlan coursePlan = coursePlanRepository.findById(planId)
            .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        if (findSemesterCourses(coursePlan, semester).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Semester " + semester + " already exists in course plan");
        }

        SemesterCourses newSemester = new SemesterCourses(semester, new ArrayList<>(), coursePlan);
        coursePlan.getSemesterCoursesList().add(newSemester);
        return coursePlanRepository.save(coursePlan);
    }

    @PutMapping("/{planId}/{semester}/{courseId}")
    public CoursePlan addCourse(@PathVariable Long planId, @PathVariable String semester, @PathVariable String courseId) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
            .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        validateSemester(semester);
        
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        SemesterCourses semesterCourses = findSemesterCourses(coursePlan, semester)
            .orElseThrow(() -> new SemesterNotFoundException(semester, planId));

        if (!semesterCourses.getCourses().contains(courseId)) {
            semesterCourses.getCourses().add(courseId);
        }

        return coursePlanRepository.save(coursePlan);
    }

    @DeleteMapping("/{planId}/{semester}/{courseCode}")
    public CoursePlan removeCourseFromSemester(
            @PathVariable Long planId,
            @PathVariable String semester,
            @PathVariable String courseCode
    ) {

        // Fetch the course plan or return error for not found
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        // Find the semester in the course plan or throw exception
        SemesterCourses semesterCourses = coursePlan.getSemesterCoursesList().stream()
                .filter(sc -> sc.getSemester().toLowerCase().equals(semester.trim().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new SemesterNotFoundException(semester, planId));

        // Remove the course if it exists
        if (!semesterCourses.getCourses().remove(courseCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find course " + courseCode + " in semester " + semester);
        }

        // If the semester has no more courses, remove it completely (Optional)
        if (semesterCourses.getCourses().isEmpty()) {
            coursePlan.getSemesterCoursesList().remove(semesterCourses);
        }

        return coursePlanRepository.save(coursePlan);
    }

    @DeleteMapping("/{planId}/{semester}")
    public CoursePlan removeSemester(
            @PathVariable Long planId,
            @PathVariable String semester
    ) {

        // Fetch the course plan or throw an exception
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        // Find and remove the course from the courseplan
        boolean removed = coursePlan.getSemesterCoursesList().removeIf(sc -> sc.getSemester().trim().toLowerCase().equals(semester.trim().toLowerCase()));

        if (!removed) {
            throw new SemesterNotFoundException(semester, planId);
        }

        return coursePlanRepository.save(coursePlan);
    }

}