package com.inde.indytrack.exception;

public class DepartmentNotFoundException extends RuntimeException{
    public DepartmentNotFoundException(String code) {
        super("Could not find department " + code);
    }
}
