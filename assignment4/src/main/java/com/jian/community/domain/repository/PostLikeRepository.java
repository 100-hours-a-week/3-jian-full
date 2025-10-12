package com.jian.community.domain.repository;

import com.jian.community.domain.model.PostLike;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository {

    public PostLike save(PostLike postLike);

    public Optional<PostLike> findById(Long postLikeId);

    public List<PostLike> findByPostId(Long postId);

    public void deleteByPostIdAndUserId(Long postId, Long userId);
}
