package com.jian.community.presentation.exception;

import com.jian.community.application.exception.ErrorCode;

public record FieldErrorResponse(ErrorCode code, String message, String field) {
}
