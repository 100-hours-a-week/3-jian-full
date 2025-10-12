package com.jian.community.application.service;

import com.jian.community.application.exception.ErrorCode;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.application.exception.ForbiddenException;
import com.jian.community.application.exception.NotFoundException;
import com.jian.community.domain.model.*;
import com.jian.community.domain.repository.CommentRepository;
import com.jian.community.domain.repository.PostRepository;
import com.jian.community.domain.repository.UserRepository;
import com.jian.community.presentation.dto.CommentResponse;
import com.jian.community.presentation.dto.CreateCommentRequest;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.UpdateCommentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentService {

    private final int COMMENT_PAGE_SIZE = 10;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CursorResponse<CommentResponse> getComments(Long postId, LocalDateTime cursor) {
        Post post = postRepository.findByIdOrThrow(postId);
        CursorPage<Comment> page = commentRepository
                .findAllByPostIdOrderByCreatedAtDesc(post.getId(), cursor, COMMENT_PAGE_SIZE);

        LocalDateTime nextCursor = page.hasNext() ? page.content().getLast().getCreatedAt() : null;
        List<CommentResponse> items = page.content().stream()
                .map(comment -> {
                    User writer = userRepository.findByIdOrThrow(comment.getUserId());

                    return new CommentResponse(
                            comment.getId(),
                            writer.getNickname(),
                            writer.getProfileImageUrl(),
                            comment.getContent(),
                            comment.getCreatedAt(),
                            comment.getUpdatedAt()
                    );
                }).toList();

        return new CursorResponse<>(items, nextCursor, page.hasNext());
    }

    public CommentResponse creatComment(Long postId, Long userId, CreateCommentRequest request) {
        Post post = postRepository.findByIdOrThrow(postId);
        User writer = userRepository.findByIdOrThrow(userId);
        Comment comment = Comment.of(post.getId(), writer.getId(), request.content());

        Comment saved = commentRepository.save(comment);

        return new CommentResponse(
                saved.getId(),
                writer.getNickname(),
                writer.getProfileImageUrl(),
                saved.getContent(),
                saved.getCreatedAt(),
                saved.getUpdatedAt()
        );
    }

    public void updateComment(Long postId, Long commentId, Long userId, UpdateCommentRequest request) {
        Post post = postRepository.findByIdOrThrow(postId);
        User writer = userRepository.findByIdOrThrow(userId);
        Comment comment = commentRepository.findByIdOrThrow(commentId);

        if (!comment.getPostId().equals(post.getId())) {
            throw new NotFoundException(
                    ErrorCode.COMMENT_NOT_EXISTS,
                    "댓글을 찾을 수 없습니다."
            );
        }
        if (!comment.getUserId().equals(writer.getId())) {
            throw new ForbiddenException(
                    ErrorCode.ACCESS_DENIED,
                    "접근 권한이 없습니다."
            );
        }

        comment.update(request.content());
        commentRepository.save(comment);
    }

    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findByIdOrThrow(postId);
        User writer = userRepository.findByIdOrThrow(userId);
        Comment comment = commentRepository.findByIdOrThrow(commentId);

        if (!comment.getPostId().equals(post.getId())) {
            throw new NotFoundException(
                    ErrorCode.COMMENT_NOT_EXISTS,
                    "댓글을 찾을 수 없습니다."
            );
        }
        if (!comment.getUserId().equals(writer.getId())) {
            throw new ForbiddenException(
                    ErrorCode.ACCESS_DENIED,
                    "접근 권한이 없습니다."
            );
        }

        commentRepository.deleteById(commentId);
    }
}
