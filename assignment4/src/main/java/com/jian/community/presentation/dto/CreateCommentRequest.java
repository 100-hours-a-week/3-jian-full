package com.jian.community.presentation.dto;

import com.jian.community.domain.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(

        @NotBlank(message = ValidationMessage.COMMENT_CONTENT_REQUIRED)
        String content
) {
}
