package com.jian.community.domain.repository;

import com.jian.community.domain.model.Post;

import java.util.Optional;

public interface PostRepository {

    public Post save(Post post);

    Optional<Post> findById(Long postId);

    void deleteById(Long postId);

    boolean existsById(Long postId);
}
