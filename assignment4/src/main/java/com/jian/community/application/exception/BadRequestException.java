package com.jian.community.application.exception;

public class BadRequestException extends CustomRuntimeException {
    public BadRequestException(ErrorCode code, String message) { super(code, message); }
}
