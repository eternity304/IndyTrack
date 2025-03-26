package com.inde.indytrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

 
@ControllerAdvice
public class MinorNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(MinorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String minorNotFoundHandler(MinorNotFoundException ex) {
        return ex.getMessage();
    }
}
