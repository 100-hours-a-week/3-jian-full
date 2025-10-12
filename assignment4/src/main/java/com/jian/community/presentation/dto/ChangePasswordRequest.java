package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.PasswordFormat;
import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(

        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        @PasswordFormat
        String oldPassword,

        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        @PasswordFormat
        String newPassword
) {
}
