package com.jian.community.presentation.dto;

import com.jian.community.presentation.validation.ValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record UpdateCommentRequest(

        @NotBlank(message = ValidationMessage.COMMENT_CONTENT_REQUIRED)
        String content
) {
}
