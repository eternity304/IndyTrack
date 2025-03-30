package com.inde.indytrack.exception;

public class InvalidCommentException extends RuntimeException {
    public InvalidCommentException() {
        super("Comment must be between 1 and 1000 characters");
    }
}
