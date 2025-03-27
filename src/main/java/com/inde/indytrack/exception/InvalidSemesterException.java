package com.inde.indytrack.exception;

public class InvalidSemesterException extends RuntimeException {
    public InvalidSemesterException(String semester) {
        super("Invalid semester format: " + semester);
    }
}
