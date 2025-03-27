package com.inde.indytrack.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.inde.indytrack.dto.RatingDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.RatingNotFoundException;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.Rating;
import com.inde.indytrack.model.RatingKey;
import com.inde.indytrack.model.Student;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.RatingRepository;
import com.inde.indytrack.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
@RestController
@RequestMapping("/ratings")
public class RatingController {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public RatingController(
        RatingRepository ratingRepository, 
        CourseRepository courseRepository, 
        StudentRepository studentRepository) {
        this.ratingRepository = ratingRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping("/{courseCode}")
    public Double retrieveAverageRating(@PathVariable String courseCode) {
        Course course = courseRepository.findById(courseCode)
            .orElseThrow(() -> new CourseNotFoundException(courseCode));

        return course.getAverageRating();
    }
    
    @PostMapping
    public Rating createRating(@RequestBody RatingDTO ratingDto) {
        Course course = courseRepository.findById(ratingDto.getCourseCode())
            .orElseThrow(() -> new CourseNotFoundException(ratingDto.getCourseCode()));

        Student student = studentRepository.findById(ratingDto.getStudentId())
            .orElseThrow(() -> new StudentNotFoundException(ratingDto.getStudentId()));
            
        if (ratingDto.getRatingValue() < 1 || ratingDto.getRatingValue() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating value must be between 1 and 5");
        }

        Rating rating = new Rating();
        rating.setId(new RatingKey(ratingDto.getCourseCode(), ratingDto.getStudentId()));
        rating.setCourse(course);
        rating.setStudent(student);
        rating.setRatingValue(ratingDto.getRatingValue());

        course.updateAverageRating(ratingDto.getRatingValue());
        courseRepository.save(course);

        return ratingRepository.save(rating);
    }

    @DeleteMapping("/{courseCode}/{studentId}")
    public void deleteRating(@PathVariable String courseCode, @PathVariable Long studentId) {
        RatingKey ratingKey = new RatingKey(courseCode, studentId);
        Rating rating = ratingRepository.findById(ratingKey)
            .orElseThrow(() -> new RatingNotFoundException(courseCode, studentId));

        ratingRepository.delete(rating);

        Course course = courseRepository.findById(courseCode)
            .orElseThrow(() -> new CourseNotFoundException(courseCode));

        course.removeRating(rating.getRatingValue());
        courseRepository.save(course);
    }

    @PutMapping("/{courseCode}/{studentId}")
    public Rating updateRating(@PathVariable String courseCode, @PathVariable Long studentId, @RequestBody RatingDTO ratingDto) {
        RatingKey ratingKey = new RatingKey(courseCode, studentId);
        Rating rating = ratingRepository.findById(ratingKey)
            .orElseThrow(() -> new RatingNotFoundException(courseCode, studentId));

        if (ratingDto.getRatingValue() < 1 || ratingDto.getRatingValue() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating value must be between 1 and 5");
        }

        Course course = courseRepository.findById(courseCode)
            .orElseThrow(() -> new CourseNotFoundException(courseCode));

        course.updateAverageRating(ratingDto.getRatingValue());
        courseRepository.save(course);

        rating.setRatingValue(ratingDto.getRatingValue());
        return ratingRepository.save(rating);
    }
}
