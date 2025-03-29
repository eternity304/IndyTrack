package com.inde.indytrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SemesterFullAdvice {
    @ResponseBody
    @ExceptionHandler(SemesterFullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String semesterFullHandler(SemesterFullException ex) {
        return ex.getMessage();
    }
}