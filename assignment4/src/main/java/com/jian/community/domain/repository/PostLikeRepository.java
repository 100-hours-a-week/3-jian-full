package com.jian.community.domain.repository;

import com.jian.community.domain.model.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository {

    PostLike save(PostLike postLike);

    Optional<PostLike> findById(Long postLikeId);

    List<PostLike> findByPostId(Long postId);

    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    void deleteByPostIdAndUserId(Long postId, Long userId);
}
