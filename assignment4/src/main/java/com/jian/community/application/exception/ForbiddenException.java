package com.jian.community.application.exception;

public class ForbiddenException extends RuntimeException {

    ErrorCode code;

    public ForbiddenException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
