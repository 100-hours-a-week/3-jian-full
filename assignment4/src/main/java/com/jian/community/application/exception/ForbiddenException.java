package com.jian.community.application.exception;

public class ForbiddenException extends CustomRuntimeException {
    public ForbiddenException(ErrorCode code, String message) { super(code, message); }
}
