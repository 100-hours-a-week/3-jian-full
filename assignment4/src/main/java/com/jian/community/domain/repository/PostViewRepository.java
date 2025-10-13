package com.jian.community.domain.repository;

import com.jian.community.application.exception.ErrorCode;
import com.jian.community.application.exception.NotFoundException;
import com.jian.community.domain.model.PostView;

import java.util.Optional;

public interface PostViewRepository {

    PostView save(PostView postView);

    Optional<PostView> findByPostId(Long postId);

    default PostView findByPostIdOrThrow(Long postId) {
        return findByPostId(postId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.POST_VIEW_NOT_EXISTS,
                        "게시글 조회수 정보를 찾을 수 없습니다."
                ));
    }
}
