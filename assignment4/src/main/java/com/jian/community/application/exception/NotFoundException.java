package com.jian.community.application.exception;

public class NotFoundException extends CustomRuntimeException {
    public NotFoundException(ErrorCode code, String message) { super(code, message); }
}
