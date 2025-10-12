package com.jian.community.presentation.dto;

import com.jian.community.domain.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(

        @NotBlank(message = ValidationMessage.COMMENT_CONTENT_REQUIRED)
        String content
) {
}
