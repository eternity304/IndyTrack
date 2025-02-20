package com.example.cms.controller;

import com.example.cms.controller.exceptions.CommentNotFoundException;
import com.example.cms.model.entity.Comment;
import com.example.cms.model.entity.CommentKey;
import com.example.cms.model.entity.Course;
import com.example.cms.model.entity.Student;
import com.example.cms.controller.dto.CommentDto;
import com.example.cms.model.repository.CommentRepository;
import com.example.cms.model.repository.CourseRepository;
import com.example.cms.model.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

import com.example.cms.model.entity.Comment;
import com.example.cms.model.repository.CommentRepository;
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

    @DeleteMapping("/comments/{commentInfo}")
    public String deleteCoursePlan(@PathVariable String commentInfo) {
        String[] parts = commentInfo.split("~");
        CommentKey commentKey = new CommentKey(Long.valueOf(parts[0]), parts[1], parts[2]);
        commentRepository.deleteById(commentKey);
        return "Course plan deleted successfully";
    }

    @GetMapping("/comments")
    List<Comment> retrieveAllComment() {
        return this.commentRepository.findAll();
    }

    @GetMapping("/comments/{code}")
    List<Comment> retrieveCourse(@PathVariable("code") String courseCode) {
        List<Comment> comments = this.commentRepository.findCommentById(courseCode);
        if (comments.isEmpty()) {
            throw new CommentNotFoundException(courseCode);
        }
        return comments;
    }

    @PostMapping("/comments/post")
    Comment createCourse(@RequestBody CommentDto commentDto) {
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
