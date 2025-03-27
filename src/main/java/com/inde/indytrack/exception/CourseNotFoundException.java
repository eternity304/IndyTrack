package com.inde.indytrack.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String code) {
        super("Could not find a course with the code " + code);
    }

    public CourseNotFoundException(Integer level) {
        super("Could not find any courses at level " + level);
    }
}
