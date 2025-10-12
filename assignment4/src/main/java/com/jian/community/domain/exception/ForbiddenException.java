package com.jian.community.domain.exception;

import com.jian.community.domain.constant.ErrorCode;

public class ForbiddenException extends RuntimeException {

    ErrorCode code;

    public ForbiddenException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
