package com.hrworkflow.workflowservice.controller;

import com.hrworkflow.workflowservice.dto.AppError;
import com.hrworkflow.workflowservice.dto.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<AppError> catchResourceNotFoundEx(ResourceNotFoundException ex) {

        return new ResponseEntity<>(
                new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppError> catchIllegalArgumentEx(IllegalArgumentException ex) {

        return new ResponseEntity<>(
                new AppError(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> catchGeneralException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }

}
