package com.inde.indytrack.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String code) {
        super("Could not find the course with code " + code);
    }

    public CourseNotFoundException(Integer level) {
        super("Could not find any courses at level " + level);
    }
}
