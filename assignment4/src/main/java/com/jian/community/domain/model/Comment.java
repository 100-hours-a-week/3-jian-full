package com.jian.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Comment extends MinimalEntity {

    private Long postId;
    private Long userId;
    private String content;
    private final Integer depth = 1; // 추후 대댓글 확장 가능

    public static Comment of(Long postId, Long userId, String content) {
        Comment comment = new Comment();
        comment.postId = postId;
        comment.userId = userId;
        comment.content = content;
        return comment;
    }

    public void update(String content) {
        this.content = content;
    }
}
