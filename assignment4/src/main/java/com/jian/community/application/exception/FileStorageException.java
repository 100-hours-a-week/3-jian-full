package com.jian.community.application.exception;

public class FileStorageException extends RuntimeException {

    ErrorCode code;

    public FileStorageException(ErrorCode code, String message) {
        super(message);
        this.code = code;
    }
}
