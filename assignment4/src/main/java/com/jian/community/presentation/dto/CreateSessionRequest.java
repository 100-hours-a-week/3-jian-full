package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.EmailFormat;
import com.jian.community.presentation.validation.PasswordFormat;
import jakarta.validation.constraints.NotBlank;

public record CreateSessionRequest(

        @NotBlank(message = ValidationMessage.EMAIL_REQUIRED)
        @EmailFormat
        String email,

        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        @PasswordFormat
        String password
) {
}
