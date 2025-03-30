package com.inde.indytrack.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String courseCode) {
        super("Review not found for course code: " + courseCode);
    }
    public ReviewNotFoundException(String courseCode, Long studentId) {
        super("Review not found for course code: " + courseCode + " and student id: " + studentId);
    }
}
