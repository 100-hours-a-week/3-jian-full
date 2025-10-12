package com.jian.community.application.exception;

public class UnauthorizedException extends RuntimeException {

    ErrorCode code;

    public UnauthorizedException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
