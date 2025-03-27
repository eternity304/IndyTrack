package com.inde.indytrack.exception;

public class CoursePlanNotFoundException extends RuntimeException {
    public CoursePlanNotFoundException(Long id) {
        super("Could not find a course plan with the ID " + id);
    }
}
