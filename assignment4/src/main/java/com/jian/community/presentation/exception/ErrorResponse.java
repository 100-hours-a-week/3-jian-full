package com.jian.community.presentation.exception;

import com.jian.community.application.exception.ErrorCode;

public record ErrorResponse(ErrorCode code, String message) {
}
