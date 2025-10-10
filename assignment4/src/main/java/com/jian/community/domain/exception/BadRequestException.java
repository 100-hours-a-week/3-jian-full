package com.jian.community.domain.exception;

import com.jian.community.domain.constant.ErrorCode;

public class BadRequestException extends RuntimeException {

    ErrorCode code;

    public BadRequestException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
