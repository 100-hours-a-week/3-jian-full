package com.jian.community.application.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomRuntimeException extends RuntimeException {

    @Getter
    ErrorCode code;

    public CustomRuntimeException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
