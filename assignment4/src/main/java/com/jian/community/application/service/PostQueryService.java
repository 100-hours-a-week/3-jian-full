package com.jian.community.application.service;

import com.jian.community.application.mapper.CommentDtoMapper;
import com.jian.community.application.mapper.CursorPageMapper;
import com.jian.community.application.mapper.PostDtoMapper;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.*;
import com.jian.community.domain.repository.*;
import com.jian.community.presentation.dto.CommentResponse;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.PostDetailResponse;
import com.jian.community.presentation.dto.PostResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostQueryService {

    private final int POST_PAGE_SIZE = 10;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostViewRepository postViewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostViewService postViewService;

    public CursorResponse<PostResponse> getPosts(LocalDateTime cursor) {
        CursorPage<Post> page = postRepository.findAllOrderByCreatedAtDesc(cursor, POST_PAGE_SIZE);

        return CursorPageMapper.toCursorResponse(page, post -> {
            User writer = userRepository.findByIdOrThrow(post.getUserId());
            List<PostLike> likes = postLikeRepository.findByPostId(post.getId());
            List<Comment> comments = commentRepository.findByPostId(post.getId());
            PostView view = postViewRepository.findByPostIdOrThrow(post.getId());

            return PostDtoMapper.toPostResponse(post, writer, likes.size(), comments.size(), view.getCount());
        });
    }

    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findByIdOrThrow(postId);
        User writer = userRepository.findByIdOrThrow(post.getUserId());
        List<PostLike> likes = postLikeRepository.findByPostId(postId);
        CursorResponse<CommentResponse> commentPreview = getRecentComments(postId);
        PostView view = postViewService.increaseAndGet(postId);

        return PostDtoMapper.toPostDetailResponse(
                post, writer,
                likes.size(), commentPreview.getItems().size(), view.getCount(),
                commentPreview
        );
    }

    private CursorResponse<CommentResponse> getRecentComments(Long postId) {
        CursorPage<Comment> page = commentRepository
                .findAllByPostIdOrderByCreatedAtDesc(postId, null, 10);

        return CursorPageMapper.toCursorResponse(page, comment -> {
            User writer = userRepository.findByIdOrThrow(comment.getUserId());

            return CommentDtoMapper.toCommentResponse(comment, writer);
        });
    }
}
