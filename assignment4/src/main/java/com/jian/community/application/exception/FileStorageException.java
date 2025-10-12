package com.jian.community.application.exception;

public class FileStorageException extends CustomRuntimeException {
    public FileStorageException(ErrorCode code, String message) { super(code, message); }
}
