package com.jian.community.domain.repository;

import com.jian.community.domain.exception.ErrorMessage;
import com.jian.community.domain.exception.ResourceNotFoundException;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.Post;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(Long postId);

    void deleteById(Long postId);

    CursorPage<Post> findAllOrderByCreatedAtDesc(LocalDateTime cursor, int pageSize);

    default Post findByIdOrThrow(Long postId) {
        return findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.POST_NOT_EXISTS));
    }
}
