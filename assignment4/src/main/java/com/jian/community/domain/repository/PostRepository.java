package com.jian.community.domain.repository;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Post;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface PostRepository {

    public Post save(Post post);

    Optional<Post> findById(Long postId);

    void deleteById(Long postId);

    boolean existsById(Long postId);

    CursorPage<Post> findAllOrderByCreatedAtDesc(LocalDateTime cursor, int pageSize);
}
