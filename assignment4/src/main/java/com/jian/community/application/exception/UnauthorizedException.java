package com.jian.community.application.exception;

public class UnauthorizedException extends CustomRuntimeException {
    public UnauthorizedException(ErrorCode code, String message) { super(code, message); }
}
