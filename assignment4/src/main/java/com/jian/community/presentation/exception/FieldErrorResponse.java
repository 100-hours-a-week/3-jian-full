package com.jian.community.presentation.exception;

public record FieldErrorResponse(ErrorCode code, String message, String field) {
}
