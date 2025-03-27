package com.inde.indytrack.exception;

import com.inde.indytrack.model.CommentKey;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(CommentKey id) {
        super("Could not find comment with ID " + id);
    }

    public CommentNotFoundException(String courseCode) {
        super("Could not find comment with course code " + courseCode);
    }

    public CommentNotFoundException(Long studentId) {
        super("Could not find comment written by student ID " + studentId);
    }

    public CommentNotFoundException(Long studentId, String courseCode) {
        super("Could not find comment with student ID " + studentId + " and course code " + courseCode);
    }

    public CommentNotFoundException(Long studentId, String courseCode, Long commentNumber) {
        super("Could not find comment with student ID " + studentId + " and course code " + courseCode + " and comment number " + commentNumber);
    }
}
