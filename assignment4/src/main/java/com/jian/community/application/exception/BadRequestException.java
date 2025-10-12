package com.jian.community.application.exception;

public class BadRequestException extends RuntimeException {

    ErrorCode code;

    public BadRequestException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
