package com.jian.community.domain.repository;

import com.jian.community.application.exception.ErrorCode;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.application.exception.NotFoundException;
import com.jian.community.domain.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long commentId);

    List<Comment> findByPostId(Long postId);

    CursorPage<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId, LocalDateTime cursor, int pageSize);

    void deleteById(Long commentId);

    default Comment findByIdOrThrow(Long commentId) {
        return findById(commentId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.COMMENT_NOT_EXISTS,
                        "댓글을 찾을 수 없습니다."
                ));
    }
}
