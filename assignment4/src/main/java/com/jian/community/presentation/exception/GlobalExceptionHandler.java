package com.jian.community.presentation.exception;

import com.jian.community.application.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FieldErrorResponse handleMethodArgumentNotValid(BindException e) {
        Optional<FieldError> fieldError = e.getBindingResult().getFieldErrors().stream()
                .findFirst();

        // 필드 에러가 없고 글로벌 에러만 있는 경우
        if (fieldError.isEmpty()) {
            String globalMessage = e.getBindingResult().getGlobalErrors().stream()
                    .findFirst()
                    .map(ObjectError::getDefaultMessage)
                    .orElse(ErrorMessage.INVALID_INPUT);

            return new FieldErrorResponse(ErrorCode.INVALID_USER_INPUT, globalMessage, null);
        }

        String field = fieldError.get().getField();
        String validation = fieldError.get().getCode();
        String message = fieldError.get().getDefaultMessage();
        ErrorCode code = "NotBlank".equals(validation)
                ? ErrorCode.USER_INPUT_REQUIRED
                : ErrorCode.INVALID_USER_INPUT;

        return new FieldErrorResponse(code, message, field);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(FileStorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleFileStorageException(FileStorageException e) {
        return new ErrorResponse(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException() {
        return new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                ErrorMessage.INTERNAL_SERVER_ERROR
        );
    }
}

