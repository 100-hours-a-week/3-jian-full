package com.jian.community.domain.repository;

import com.jian.community.domain.model.PostView;

import java.util.Optional;

public interface PostViewRepository {

    public PostView save(PostView postView);

    public Optional<PostView> findByPostId(Long postId);

    public void deleteByPostId(Long postId);
}
