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

    @GetMapping("/{courseCode}")
    public List<Review> retrieveReviewsByCourse(@PathVariable String courseCode) {
        List<Review> reviews = reviewRepository.findLatestReviewsByCourseCode(courseCode);
        if (reviews.isEmpty()) {
            throw new ReviewNotFoundException(courseCode);
        }
        return reviews;
    }

    @GetMapping("/{courseCode}/average-rating")
    public Double retrieveAverageRating(@PathVariable String courseCode) {
        Course course = courseRepository.findById(courseCode)
            .orElseThrow(() -> new CourseNotFoundException(courseCode));
        return course.getAverageRating();
    }

    @PostMapping
    public Review createReview(@RequestBody ReviewDTO reviewDto) {
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
        course.updateAverageRating(reviewDto.getRating());
        courseRepository.save(course);
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
        Course course = review.getCourse();
        course.removeRating(review.getRating());
        course.updateAverageRating(reviewDto.getRating());
        courseRepository.save(course);
        review.setRating(reviewDto.getRating());
        review.setComment(reviewDto.getComment());
        review.setUploadTime(LocalDateTime.now().toString());
        return reviewRepository.save(review);
    }

    @DeleteMapping("/{courseCode}/{studentId}")
    public void deleteReview(@PathVariable String courseCode, @PathVariable Long studentId) {
        Review review = reviewRepository.findById(new ReviewKey(courseCode, studentId))
            .orElseThrow(() -> new ReviewNotFoundException(courseCode, studentId));
        Course course = review.getCourse();
        course.removeRating(review.getRating());
        courseRepository.save(course);
        reviewRepository.delete(review);
    }
}
