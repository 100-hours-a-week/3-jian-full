package com.jian.community.domain.model;

import com.jian.community.application.exception.ErrorCode;
import com.jian.community.application.exception.ErrorMessage;
import com.jian.community.application.exception.ForbiddenException;
import com.jian.community.application.exception.NotFoundException;
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

    public void validatePost(Post post) {
        if (postId.equals(post.getId())) {
            throw new NotFoundException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    ErrorMessage.COMMENT_NOT_EXISTS
            );
        }
    }

    public void validateWriter(User writer){
        if (userId.equals(writer.getId())) {
            throw new ForbiddenException(
                    ErrorCode.ACCESS_DENIED,
                    ErrorMessage.ACCESS_DENIED
            );
        }
    }
}
