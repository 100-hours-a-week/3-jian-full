package com.jian.community.presentation.dto;

import com.jian.community.domain.constant.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdatePostRequest(

        @NotBlank(message = ValidationMessage.POST_TITLE_REQUIRED)
        @Size(max = 26, message = ValidationMessage.INVALID_POST_TITLE)
        String title,

        @NotBlank(message = ValidationMessage.POST_CONTENT_REQUIRED)
        String content,

        List<String> postImageUrls
) {
}
