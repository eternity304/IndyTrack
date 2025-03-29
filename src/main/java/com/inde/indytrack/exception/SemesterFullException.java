package com.inde.indytrack.exception;

public class SemesterFullException extends RuntimeException {
    public SemesterFullException(String semester) {
        super("Cannot add more than 6 courses to " + semester + " semester");
    }
}
