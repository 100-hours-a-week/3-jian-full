package com.jian.community.domain.exception;

import com.jian.community.domain.constant.ErrorCode;

public class NotFoundException extends RuntimeException {

    ErrorCode code;

    public NotFoundException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
