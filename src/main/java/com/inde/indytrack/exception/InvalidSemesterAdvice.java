package com.inde.indytrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidSemesterAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidSemesterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidSemesterHandler(InvalidSemesterException ex) {
        return ex.getMessage();
    }
}
