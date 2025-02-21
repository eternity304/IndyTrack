package com.inde.indytrack.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String code) {super("Could not find courseId " +  code); }
}
