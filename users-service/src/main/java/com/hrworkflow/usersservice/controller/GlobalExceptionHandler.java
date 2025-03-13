package com.hrworkflow.usersservice.controller;

import com.hrworkflow.usersservice.dto.AppError;
import com.hrworkflow.usersservice.dto.ResourceNotFoundException;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> catchGeneralException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
    }
}
