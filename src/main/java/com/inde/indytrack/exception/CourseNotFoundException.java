package com.inde.indytrack.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String code) {
        super("Could not find course " + code);
    }
}
