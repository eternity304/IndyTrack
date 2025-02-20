package com.example.cms.controller.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String code) {super("Could not find courseId " +  code); }
}
