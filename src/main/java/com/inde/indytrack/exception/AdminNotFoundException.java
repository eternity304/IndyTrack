package com.inde.indytrack.exception;

public class AdminNotFoundException extends RuntimeException {
    public AdminNotFoundException(Long id) {
        super("Could not find admin " + id);
    }
}