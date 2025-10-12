package com.jian.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostLike extends MinimalEntity {

    Long postId;
    Long userId;

    public static PostLike of(Long postId, Long userId) {
        return new PostLike(postId, userId);
    }
}
