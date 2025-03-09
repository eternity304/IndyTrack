package com.inde.indytrack.controller;

import com.inde.indytrack.exception.CommentNotFoundException;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.entity.Comment;
import com.inde.indytrack.entity.CommentKey;
import com.inde.indytrack.entity.Course;
import com.inde.indytrack.entity.Student;
import com.inde.indytrack.dto.CommentDTO;
import com.inde.indytrack.repository.CommentRepository;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private final CommentRepository commentRepository;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private final CourseRepository courseRepository;

    public CommentController(
            CommentRepository commentRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ) {
        this.commentRepository = commentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    List<Comment> retrieveAllComments() {
        return this.commentRepository.findAll();
    }

    @GetMapping("/{code}")
    List<Comment> retrieveCommentsByCourseCode(@PathVariable("code") String courseCode) {
        List<Comment> comments = this.commentRepository.findCommentByCourseCode(courseCode);
        if (comments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No comments found for course with code " + courseCode);
        }
        return comments;
    }

    @PostMapping("/post")
    Comment createCourse(@RequestBody CommentDTO commentDto) {
        Student student = this.studentRepository.findById(commentDto.getStudentId()).orElseThrow(() -> new StudentNotFoundException(commentDto.getStudentId()));
        Course course = this.courseRepository.findById(commentDto.getCourseCode()).orElseThrow(() -> new CourseNotFoundException(commentDto.getCourseCode()));

        if (student != null && course != null && !commentDto.getBody().isEmpty()) {
            Comment newComment = new Comment();
            CommentKey key = new CommentKey();

            key.setStudentId(student.getId());
            key.setCourseCode(course.getCode());
            key.setTime(LocalDateTime.now().toString());
            newComment.setCommentId(key);
            newComment.setCourse(course);
            newComment.setStudent(student);
            newComment.setTime(LocalDateTime.now().toString());
            newComment.setBody(commentDto.getBody());

            return this.commentRepository.save(newComment);
        } else {
            throw new RuntimeException("Could not create comment: invalid student or course Id.");
        }
    }

}