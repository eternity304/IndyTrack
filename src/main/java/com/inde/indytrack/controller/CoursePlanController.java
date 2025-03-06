package com.inde.indytrack.controller;

import com.inde.indytrack.dto.CoursePlanDTO;
import com.inde.indytrack.entity.Course;
import com.inde.indytrack.entity.CoursePlan;
import com.inde.indytrack.entity.Student;
import com.inde.indytrack.entity.SemesterCourses;
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
        if (!studentRepository.existsById(studentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with student ID " + studentId + " has been found");
        }
        return coursePlanRepository.findByStudentId(studentId);
    }

    @GetMapping("/{planId}")
    public Optional<CoursePlan> getCoursePlanById(@PathVariable Long planId) {
        if (!coursePlanRepository.existsById(planId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course plan with ID " + planId + " was not found");
        }
        return coursePlanRepository.findById(planId);
    }

    @PostMapping
    public CoursePlan createCoursePlan(@RequestBody CoursePlanDTO coursePlanDTO) {
        Student student = studentRepository.findById(coursePlanDTO.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with ID " + coursePlanDTO.getStudentId() + " was not found"));

        CoursePlan coursePlan = new CoursePlan(student, coursePlanDTO.getSemesterCourses());
        return coursePlanRepository.save(coursePlan);
    }

    @DeleteMapping("/{planId}")
    public String deleteCoursePlan(@PathVariable Long planId) {
        if (!coursePlanRepository.existsById(planId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Plan with ID " + planId + " does not exist");
        }
        coursePlanRepository.deleteById(planId);
        return "Course plan with ID " + planId + " deleted successfully";
    }

    private boolean checkSemester(String semester) {
        String[] parts = semester.split(" ");

        // Check if entry is correct
        if (parts.length != 2) { return false; }

        String term = parts[0].trim().toLowerCase();
        int year;

        // Check if year has valid value
        try {
            year = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            return false;
        }

        // Check if year is within valid range
        if (!(year >= 2018 && year <= 2030)) {
            return false;
        }

        switch (term) {
            case "winter": return true;
            case "summer": return true;
            case "fall": return true;
            default: return false;
        }

    }

    private int getSemesterOrder(String semester) {
        /*
        Converts a semester string (e.g., "Fall 2020") into a comparable integer.
        Winter -> 1, Summer -> 2, Fall -> 3.
         */
        String[] parts = semester.split(" ");
        if (parts.length != 2) { throw new IllegalArgumentException("Invalid semester format: " + semester); }

        String term = parts[0].trim().toLowerCase();
        int year;
        try { 
            year = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid year in semester format: " + semester);
        }

        int termOrder;
        switch (term.toLowerCase()) {
            case "winter": termOrder = 1; break;
            case "summer": termOrder = 2; break;
            case "fall": termOrder = 3; break;
            default: throw new IllegalArgumentException("Unknown semester term: " + term);
        }

        return year * 10 + termOrder;
    }

    private boolean isCourseCompletedInPreviousSemesters(Course prerequisite, List<SemesterCourses> semesters, int currentSemesterOrder) {
        /**
        Checks if a prerequisite course was completed in any semester before the current one.
        */
        for (SemesterCourses sc : semesters) {
            if (getSemesterOrder(sc.getSemester()) < currentSemesterOrder && sc.getCourses().contains(prerequisite.getCode())) {
                return true;  
            }
        }

        for (Course prereqPrerequisite : prerequisite.getPrerequisites()) {
            if (!isCourseCompletedInPreviousSemesters(prereqPrerequisite, semesters, currentSemesterOrder)) {
                return false;
            }
        }

        return false;
    }
    @PutMapping("/{planId}/{semester}")
    public CoursePlan addSemester(
        @PathVariable Long planId,
        @PathVariable String semester
    ) {
        if (!checkSemester(semester)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Semester " + semester + " is invalid");
        }

        CoursePlan coursePlan = coursePlanRepository.findById(planId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find course plan")); 
        
        List<SemesterCourses> semesters = coursePlan.getSemesterCoursesList();

        Optional<SemesterCourses> optionalSemesterCourses = semesters.stream()
            .filter(sc -> sc.getSemester() == semester)
            .findFirst();
        
        if (!optionalSemesterCourses.isPresent()) {
            SemesterCourses semesterCourses = new SemesterCourses(semester, new ArrayList<>(), coursePlan);
            coursePlan.getSemesterCoursesList().add(semesterCourses);
        }

        return coursePlanRepository.save(coursePlan);
    }

    @PutMapping("/{planId}/{semester}/{courseId}")
    public CoursePlan addCourse(
        @PathVariable Long planId,
        @PathVariable String semester,
        @PathVariable String courseId
    ) {
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find course plan"));
        
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not course: " + courseId));
        
        int currentSemesterOrder = getSemesterOrder(semester);

        List<SemesterCourses> semesters = coursePlan.getSemesterCoursesList();
        
        Optional<SemesterCourses> optionalSemesterCourses = semesters.stream()
            .filter(sc -> getSemesterOrder(sc.getSemester()) == currentSemesterOrder)
            .findFirst();
        
        if (!optionalSemesterCourses.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Did not find semester " + semester + " in course plan");
        }

        SemesterCourses semesterCourses = optionalSemesterCourses.get();
    
        // Check prereq
        Set<Course> prerequisites = course.getPrerequisites();
        List<String> missingPrerequisites = new ArrayList<>();

        for (Course prerequisite : prerequisites) {
            if (!isCourseCompletedInPreviousSemesters(prerequisite, semesters, currentSemesterOrder)) {
                missingPrerequisites.add(prerequisite.getCode());
            }
        }

        if (!missingPrerequisites.isEmpty()) {
            System.err.println("Course " + courseId + " has missing prerequisite(s): " + missingPrerequisites);
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
            @PathVariable String courseCode
    ) {

        // Fetch the course plan or return error for not found
        CoursePlan coursePlan = coursePlanRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Plan not found"));

        // Find the semester in the course plan or throw exception
        SemesterCourses semesterCourses = coursePlan.getSemesterCoursesList().stream()
                .filter(sc -> sc.getSemester().toLowerCase().equals(semester.trim().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semester not found"));

        // Remove the course if it exists
        if (!semesterCourses.getCourses().remove(courseCode)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found in the semester");
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course Plan not found"));

        // Find and remove the course from the courseplan
        boolean removed = coursePlan.getSemesterCoursesList().removeIf(sc -> sc.getSemester().trim().toLowerCase().equals(semester.trim().toLowerCase()));

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Semester not found");
        }

        return coursePlanRepository.save(coursePlan);
    }

}