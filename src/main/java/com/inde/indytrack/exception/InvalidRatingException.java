package com.inde.indytrack.exception;

public class InvalidRatingException extends RuntimeException {
    public InvalidRatingException() {
        super("Rating must be between 1 and 5");
    }
}
