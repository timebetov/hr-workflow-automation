package com.hrworkflow.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomServiceException extends RuntimeException {

    private final HttpStatus status;

    public CustomServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CustomServiceException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST; // По умолчанию 400
    }
}
