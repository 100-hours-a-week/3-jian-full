package com.jian.community.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(

        @NotBlank
        @Email(
                message = "올바르지 않은 이메일 형식입니다."
        )
        String email,

        @NotBlank
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "비밀번호는 8자 이상이며, 영문과 숫자를 모두 포함해야 합니다."
        )
        String password,

        @NotBlank
        @Pattern(
                regexp = "^[가-힣a-zA-Z0-9]{2,20}$",
                message = "닉네임은 2~20자의 한글, 영문, 숫자만 사용할 수 있습니다."
        )
        String nickname,

        @NotBlank
        String profileImageUrl
) {
}
