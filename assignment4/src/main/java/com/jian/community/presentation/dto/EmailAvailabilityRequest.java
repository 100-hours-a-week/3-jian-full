package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import com.jian.community.presentation.validation.EmailFormat;
import jakarta.validation.constraints.NotBlank;

public record EmailAvailabilityRequest(

        @NotBlank(message = ValidationMessage.EMAIL_REQUIRED)
        @EmailFormat
        String email
) {
}
