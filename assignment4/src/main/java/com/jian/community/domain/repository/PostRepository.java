package com.jian.community.domain.repository;

import com.jian.community.domain.constant.ErrorCode;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.exception.NotFoundException;
import com.jian.community.domain.model.Post;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository {

    public Post save(Post post);

    Optional<Post> findById(Long postId);

    void deleteById(Long postId);

    boolean existsById(Long postId);

    CursorPage<Post> findAllOrderByCreatedAtDesc(LocalDateTime cursor, int pageSize);

    default Post findByIdOrThrow(Long postId) {
        return findById(postId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.POST_NOT_EXISTS,
                        "게시글을 찾을 수 없습니다."
                ));
    }
}
