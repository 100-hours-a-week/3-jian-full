package com.jian.community.domain.exception;

import com.jian.community.domain.constant.ErrorCode;

public class UnauthorizedException extends RuntimeException {

    ErrorCode code;

    public UnauthorizedException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
