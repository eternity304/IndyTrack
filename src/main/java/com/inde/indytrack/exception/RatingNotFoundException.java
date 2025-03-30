package com.inde.indytrack.exception;

public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(String courseCode, Long studentId) {
        super("Rating not found for course code " + courseCode + " and student ID " + studentId);
    }
}
