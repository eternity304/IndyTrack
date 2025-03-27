package com.inde.indytrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CoursePlanNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CoursePlanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String coursePlanNotFoundHandler(CoursePlanNotFoundException ex) {
        return ex.getMessage();
    }
}
