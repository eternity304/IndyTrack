package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CoursePlanDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.CoursePlanNotFoundException;
import com.inde.indytrack.exception.InvalidSemesterException;
import com.inde.indytrack.exception.SemesterFullException;
import com.inde.indytrack.exception.SemesterNotFoundException;
import com.inde.indytrack.exception.StudentNotFoundException;
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
import java.util.ArrayList;

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
        if (semester.contains(" ")) {
            throw new InvalidSemesterException(semester);
        }

        int yearIndex = -1;
        for (int i = 0; i < semester.length(); i++) {
            if (Character.isDigit(semester.charAt(i))) {
                yearIndex = i;
                break;
            }
        }

        if (yearIndex == -1) {
            throw new InvalidSemesterException(semester);
        }

        String term = semester.substring(0, yearIndex).trim().toLowerCase();
        String yearString = semester.substring(yearIndex);

        int year;
        try {
            year = Integer.parseInt(yearString);
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
    private SemesterCourses findSemesterCourses(CoursePlan coursePlan, String semester, Long planId) {
        return coursePlan.getSemesterCoursesList().stream()
                .filter(sc -> sc.getSemester().equalsIgnoreCase(semester))
                .findFirst()
                .orElseThrow(() -> new SemesterNotFoundException(semester, planId));
    }

    @PutMapping("/{planId}/{semester}")
    public CoursePlan addSemester(@PathVariable Long planId, @PathVariable String semester) {
        validateSemester(semester);

        CoursePlan coursePlan = coursePlanRepository.findById(planId)
            .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        try {
            findSemesterCourses(coursePlan, semester, planId);
            return coursePlan; // Semester already exists
        } catch (SemesterNotFoundException e) {
            // Semester doesn't exist, create new one
            SemesterCourses newSemester = new SemesterCourses(semester, new ArrayList<>(), coursePlan);
            coursePlan.getSemesterCoursesList().add(newSemester);
            return coursePlanRepository.save(coursePlan);
        }
    }

    @PutMapping("/{planId}/{semester}/{courseId}")
    public CoursePlan addCourse(@PathVariable Long planId, @PathVariable String semester, @PathVariable String courseId) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
            .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        validateSemester(semester);
        
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        SemesterCourses semesterCourses = findSemesterCourses(coursePlan, semester, planId);

        if (semesterCourses.getCourses().size() >= 6) {
            throw new SemesterFullException(semester);
        }

        if (!semesterCourses.getCourses().contains(courseId)) {
            semesterCourses.getCourses().add(courseId);
        }

        return coursePlanRepository.save(coursePlan);
    }

    @DeleteMapping("/{planId}/{semester}/{courseCode}")
    public CoursePlan removeCourseFromSemester(
            @PathVariable Long planId,
            @PathVariable String semester,
            @PathVariable String courseCode) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        SemesterCourses semesterCourses = findSemesterCourses(coursePlan, semester, planId);

        if (!semesterCourses.getCourses().remove(courseCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "Did not find course " + courseCode + " in semester " + semester);
        }

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

    @DeleteMapping("/{planId}/{semester}/clear")
    public CoursePlan clearSemesterCourses(@PathVariable Long planId, @PathVariable String semester) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new CoursePlanNotFoundException(planId));

        SemesterCourses semesterCourses = findSemesterCourses(coursePlan, semester, planId);

        semesterCourses.setCourses(new ArrayList<>());

        return coursePlanRepository.save(coursePlan);
    }


}