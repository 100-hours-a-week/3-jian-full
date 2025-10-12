package com.jian.community.application.exception;

public class NotFoundException extends RuntimeException {

    ErrorCode code;

    public NotFoundException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
