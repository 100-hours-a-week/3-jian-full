package com.jian.community.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NicknameAvailabilityRequest(

        @NotBlank
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9]{2,20}$",
                message = "닉네임은 2~20자의 한글, 영문, 숫자만 사용할 수 있습니다."
        )
        String nickname
) {
}
