package com.inde.indytrack.controller;

import com.inde.indytrack.exception.CommentNotFoundException;
import com.inde.indytrack.exception.CourseNotFoundException;
import com.inde.indytrack.exception.StudentNotFoundException;
import com.inde.indytrack.model.Comment;
import com.inde.indytrack.model.CommentKey;
import com.inde.indytrack.model.Course;
import com.inde.indytrack.model.Student;
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
    private final CommentRepository repository;
    
    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final CourseRepository courseRepository;

    public CommentController(CommentRepository repository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    List<Comment> retrieveAllComments() {
        return repository.findAll();
    }

    @GetMapping("/{courseCode}/oldest")
    List<Comment> retrieveOldestCommentsByCourseCode(@PathVariable("courseCode") String courseCode) {
        List<Comment> comments = repository.findByIdCourseCode(courseCode);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException(courseCode);
        }
        return comments;
    }

    @GetMapping("/{courseCode}/latest")
    List<Comment> retrieveLatestCommentsByCourseCode(@PathVariable("courseCode") String courseCode) {
        List<Comment> comments = repository.findLatestCommentsByCourseCode(courseCode);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException(courseCode);
        }
        return comments;
    }

    @PostMapping
    Comment createComment(@RequestBody CommentDTO commentDto) {
        Student student = studentRepository.findById(commentDto.getStudentId())
            .orElseThrow(() -> new StudentNotFoundException(commentDto.getStudentId()));

        Course course = courseRepository.findById(commentDto.getCourseCode())
            .orElseThrow(() -> new CourseNotFoundException(commentDto.getCourseCode()));

        if (commentDto.getBody().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The comment body cannot be empty");
        }

        Long commentNumber = (long) (repository.findByIdStudentIdAndIdCourseCode(student.getId(), course.getCode()).size() + 1);
        Comment newComment = new Comment();
        CommentKey key = new CommentKey(student.getId(), course.getCode(), commentNumber);

        newComment.setId(key);
        newComment.setCourse(course);
        newComment.setStudent(student);
        newComment.setUploadTime(LocalDateTime.now().toString());
        newComment.setBody(commentDto.getBody());

        return repository.save(newComment);
    }

    @PutMapping("/{courseCode}/{studentId}/{commentNumber}")
    Comment updateComment(
        @PathVariable("courseCode") String courseCode, 
        @PathVariable("studentId") Long studentId, 
        @PathVariable("commentNumber") Long commentNumber, 
        @RequestBody CommentDTO commentDto
    ) {
        CommentKey key = new CommentKey(studentId, courseCode, commentNumber);
        return repository.findById(key)
            .map(existingComment -> {
                existingComment.setBody(commentDto.getBody());
                return repository.save(existingComment);
            })
            .orElseThrow(() -> new CommentNotFoundException(studentId, courseCode, commentNumber));
    }

    @DeleteMapping("/{courseCode}/{studentId}/{commentNumber}")
    void deleteComment(@PathVariable("courseCode") String courseCode, @PathVariable("studentId") Long studentId, @PathVariable("commentNumber") Long commentNumber) {
        CommentKey key = new CommentKey(studentId, courseCode, commentNumber);
        Comment comment = repository.findById(key)
            .orElseThrow(() -> new CommentNotFoundException(studentId, courseCode, commentNumber));
        repository.delete(comment);
    }
}