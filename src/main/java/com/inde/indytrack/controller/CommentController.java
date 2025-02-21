package com.inde.indytrack.controller;

import com.inde.indytrack.exception.CommentNotFoundException;
import com.inde.indytrack.entity.Comment;
import com.inde.indytrack.entity.CommentKey;
import com.inde.indytrack.entity.Course;
import com.inde.indytrack.entity.Student;
import com.inde.indytrack.dto.CommentDTO;
import com.inde.indytrack.repository.CommentRepository;
import com.inde.indytrack.repository.CourseRepository;
import com.inde.indytrack.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import com.inde.indytrack.entity.Comment;
import com.inde.indytrack.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin
@RestController
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

    @GetMapping("/comments")
    List<Comment> retrieveAllComment() {
        return this.commentRepository.findAll();
    }

    @GetMapping("/comments/{code}")
    List<Comment> retrieveCourse(@PathVariable("code") String courseCode) {
        List<Comment> comments = this.commentRepository.findCommentByCourseId(courseCode);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException(courseCode);
        }
        return comments;
    }

    @PostMapping("/comments/post")
    Comment createCourse(@RequestBody CommentDTO commentDto) {
        List<Student> student = this.studentRepository.findStudentById(commentDto.getStudentId());
        List<Course> course = this.courseRepository.findCourseById(commentDto.getCourseId());

        if (!student.isEmpty() && !course.isEmpty() && !commentDto.getBody().isEmpty()) {
            Comment newComment = new Comment();
            CommentKey key = new CommentKey();

            key.setStudentId(student.get(0).getId());
            key.setCourseId(course.get(0).getCode());
            key.setTime(LocalDateTime.now().toString());
            newComment.setCommentId(key);
            newComment.setCourse(course.get(0));
            newComment.setStudent(student.get(0));
            newComment.setTime(LocalDateTime.now().toString());
            newComment.setBody(commentDto.getBody());

            return this.commentRepository.save(newComment);
        } else {
            throw new RuntimeException("Could not create comment: invalid student or course Id.");
        }
    }
}
