package com.inde.indytrack.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inde.indytrack.dto.ReviewDTO;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.InvalidCommentException;
import com.inde.indytrack.exception.InvalidRatingException;
import com.inde.indytrack.exception.ReviewNotFoundException;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.Review;
import com.inde.indytrack.model.ReviewKey;
import com.inde.indytrack.model.Student;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.ReviewRepository;
import com.inde.indytrack.repository.StudentRepository;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ReviewController(
        ReviewRepository reviewRepository,
        StudentRepository studentRepository,
        CourseRepository courseRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Review> retrieveAllReviews() {
        return reviewRepository.findAll();
    }

    @GetMapping("/courses/{courseCode}")
    public List<Review> retrieveReviewsByCourse(@PathVariable String courseCode) {
        if (!courseRepository.existsById(courseCode)) {
            throw new CourseNotFoundException(courseCode);
        }
        return reviewRepository.findByCourseCode(courseCode);
    }

    @GetMapping("/students/{studentId}")
    public List<Review> retrieveReviewsByStudent(@PathVariable Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new StudentNotFoundException(studentId);
        }
        return reviewRepository.findByStudentId(studentId);
    }

    @GetMapping("/{courseCode}/{studentId}")
    public Review retrieveReview(@PathVariable String courseCode, @PathVariable Long studentId) {
        return reviewRepository.findById(new ReviewKey(courseCode, studentId))
            .orElseThrow(() -> new ReviewNotFoundException(courseCode, studentId));
    }

    @GetMapping("/{courseCode}/average-rating")
    public Double retrieveAverageRating(@PathVariable String courseCode) {
        if (!courseRepository.existsById(courseCode)) {
            throw new CourseNotFoundException(courseCode);
        }
        return courseRepository.calculateAverageRating(courseCode);
    }

    @PostMapping("/{courseCode}/{studentId}")
    public Review createReview(@PathVariable String courseCode, @PathVariable Long studentId, @RequestBody ReviewDTO reviewDto) {
        if (reviewDto.getRating() < 1 || reviewDto.getRating() > 5) {
            throw new InvalidRatingException();
        }
        if (reviewDto.getComment() == null || reviewDto.getComment().isEmpty()) {
            throw new InvalidCommentException();
        }
        Course course = courseRepository.findById(reviewDto.getCourseCode())
            .orElseThrow(() -> new CourseNotFoundException(reviewDto.getCourseCode()));
        Student student = studentRepository.findById(reviewDto.getStudentId())
            .orElseThrow(() -> new StudentNotFoundException(reviewDto.getStudentId()));
        
        Review review = new Review();
        review.setId(new ReviewKey(course.getCode(), student.getId()));
        review.setCourse(course);
        review.setStudent(student);
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setUploadTime(LocalDateTime.now().toString());

        return reviewRepository.save(review);
    }

    @PutMapping("/{courseCode}/{studentId}")
    public Review updateReview(
        @PathVariable String courseCode,
        @PathVariable Long studentId,
        @RequestBody ReviewDTO reviewDto
    ) {
        Review review = reviewRepository.findById(new ReviewKey(courseCode, studentId))
            .orElseThrow(() -> new ReviewNotFoundException(courseCode, studentId));
        if (reviewDto.getRating() != null && (reviewDto.getRating() < 1 || reviewDto.getRating() > 5)) {
            throw new InvalidRatingException();
        }
        if (reviewDto.getComment() != null && (reviewDto.getComment().isEmpty())) {
            throw new InvalidCommentException();
        }

        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setUploadTime(LocalDateTime.now().toString());

        return reviewRepository.save(review);
    }

    @DeleteMapping("/{courseCode}/{studentId}")
    public void deleteReview(@PathVariable String courseCode, @PathVariable Long studentId) {
        Review review = reviewRepository.findById(new ReviewKey(courseCode, studentId))
            .orElseThrow(() -> new ReviewNotFoundException(courseCode, studentId));

        reviewRepository.delete(review);
    }
}
