package com.jian.community.presentation.dto;

import com.jian.community.domain.constant.ValidationMessage;
import com.jian.community.presentation.validation.NicknameFormat;
import jakarta.validation.constraints.NotBlank;

public record NicknameAvailabilityRequest(

        @NotBlank(message = ValidationMessage.NICKNAME_REQUIRED)
        @NicknameFormat
        String nickname
) {
}
