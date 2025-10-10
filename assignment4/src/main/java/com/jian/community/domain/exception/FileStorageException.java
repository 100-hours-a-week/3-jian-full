package com.jian.community.domain.exception;

import com.jian.community.domain.constant.ErrorCode;

public class FileStorageException extends RuntimeException {

    ErrorCode code;

    public FileStorageException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
