package com.jian.community.application.service;

import com.jian.community.application.mapper.CommentDtoMapper;
import com.jian.community.application.mapper.CursorPageMapper;
import com.jian.community.domain.dto.CursorPage;
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

@Service
@AllArgsConstructor
public class CommentService {

    private static final int COMMENT_PAGE_SIZE = 10;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CursorResponse<CommentResponse> getComments(Long postId, LocalDateTime cursor) {
        Post post = postRepository.findByIdOrThrow(postId);
        CursorPage<Comment> page = commentRepository
                .findAllByPostIdOrderByCreatedAtDesc(post.getId(), cursor, COMMENT_PAGE_SIZE);

        return CursorPageMapper.toCursorResponse(page, comment -> {
            User writer = userRepository.findByIdOrThrow(comment.getUserId());

            return CommentDtoMapper.toCommentResponse(comment, writer);
        });
    }

    public CursorResponse<CommentResponse> getRecentComments(Long postId) {
        return getComments(postId, null);
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

        comment.validatePost(post);
        comment.validateWriter(writer);

        comment.update(request.content());
        commentRepository.save(comment);
    }

    public void deleteComment(Long postId, Long commentId, Long userId) {
        Post post = postRepository.findByIdOrThrow(postId);
        User writer = userRepository.findByIdOrThrow(userId);
        Comment comment = commentRepository.findByIdOrThrow(commentId);

        comment.validatePost(post);
        comment.validateWriter(writer);

        commentRepository.deleteById(commentId);
    }
}
