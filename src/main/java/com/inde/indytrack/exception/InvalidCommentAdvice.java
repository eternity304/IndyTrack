package com.inde.indytrack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InvalidCommentAdvice {
    @ResponseBody
    @ExceptionHandler(InvalidCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String invalidCommentHandler(InvalidCommentException ex) {
        return ex.getMessage();
    }
}
