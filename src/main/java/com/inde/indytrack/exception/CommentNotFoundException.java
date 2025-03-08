package com.inde.indytrack.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(Long commentId) {
        super("Could not find comment with ID " + commentId);
    }
}
