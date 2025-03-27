package com.inde.indytrack.exception;

public class SemesterNotFoundException extends RuntimeException {
    public SemesterNotFoundException(String semester, Long planId) {
        super("Semester " + semester + " not found in course plan with the ID " + planId);
    }
}
