package com.jian.community.presentation.dto;

import com.jian.community.domain.constant.ValidationMessage;
import com.jian.community.presentation.validation.EmailFormat;
import com.jian.community.presentation.validation.NicknameFormat;
import com.jian.community.presentation.validation.PasswordFormat;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequest(

        @NotBlank(message = ValidationMessage.EMAIL_REQUIRED)
        @EmailFormat
        String email,

        @NotBlank(message = ValidationMessage.PASSWORD_REQUIRED)
        @PasswordFormat
        String password,

        @NotBlank(message = ValidationMessage.NICKNAME_REQUIRED)
        @NicknameFormat
        String nickname,

        @NotBlank(message = ValidationMessage.PROFILE_IMAGE_URL_REQUIRED)
        String profileImageUrl
) {
}
