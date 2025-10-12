package com.jian.community.presentation.dto;

import java.time.LocalDateTime;

public record CommentResponse(

        String writerNickname,
        String writerProfileImageUrl,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
