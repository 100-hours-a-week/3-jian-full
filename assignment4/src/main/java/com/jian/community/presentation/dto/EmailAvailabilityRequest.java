package com.jian.community.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailAvailabilityRequest(

        @NotBlank
        @Email(
                message = "올바르지 않은 이메일 형식입니다."
        )
        String email
) {
}
