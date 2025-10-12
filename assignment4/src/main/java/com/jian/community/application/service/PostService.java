package com.jian.community.application.service;

import com.jian.community.domain.constant.ErrorCode;
import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.exception.ForbiddenException;
import com.jian.community.domain.model.*;
import com.jian.community.domain.repository.*;
import com.jian.community.presentation.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final int POST_PAGE_SIZE = 10;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostViewRepository postViewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CursorResponse<PostResponse> getPosts(LocalDateTime cursor) {
        CursorPage<Post> page = postRepository.findAllOrderByCreatedAtDesc(cursor, POST_PAGE_SIZE);

        LocalDateTime nextCursor = page.hasNext() ? page.content().getLast().getCreatedAt() : null;
        List<PostResponse> items = page.content().stream()
                .map(post -> {
                    User writer = userRepository.findByIdOrThrow(post.getUserId());
                    List<PostLike> likes = postLikeRepository.findByPostId(post.getId());
                    List<Comment> comments = commentRepository.findByPostId(post.getId());
                    PostView postView = postViewRepository.findByPostIdOrThrow(post.getId());

                    return new PostResponse(
                            post.getId(),
                            post.getTitle(),
                            writer.getNickname(),
                            writer.getProfileImageUrl(),
                            likes.size(),
                            comments.size(),
                            postView.getCount(),
                            post.getCreatedAt(),
                            post.getUpdatedAt()
                    );
                }).toList();

        return new CursorResponse<>(items, nextCursor, page.hasNext());
    }

    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findByIdOrThrow(postId);
        User postWriter = userRepository.findByIdOrThrow(post.getUserId());
        List<PostLike> likes = postLikeRepository.findByPostId(post.getId());
        CursorPage<Comment> comments = commentRepository
                .findAllByPostIdOrderByCreatedAtDesc(post.getId(), null, 10);
        PostView postView = postViewRepository.findByPostIdOrThrow(post.getId());

        LocalDateTime nextCursor = comments.hasNext() ? comments.content().getLast().getCreatedAt() : null;
        List<CommentResponse> commentsPreviewItems = comments.content().stream()
                .map(comment -> {
                    User commentWriter = userRepository.findByIdOrThrow(comment.getUserId());
                    return new CommentResponse(
                            comment.getId(),
                            commentWriter.getNickname(),
                            commentWriter.getProfileImageUrl(),
                            comment.getContent(),
                            comment.getCreatedAt(),
                            comment.getUpdatedAt()
                    );
                }).toList();
        CursorResponse<CommentResponse> commentPreview = new CursorResponse<>(
                commentsPreviewItems,
                nextCursor,
                comments.hasNext()
        );
        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                postWriter.getNickname(),
                postWriter.getProfileImageUrl(),
                likes.size(),
                comments.content().size(),
                postView.getCount(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getContent(),
                post.getPostImageUrls(),
                commentPreview
        );
    }

    public PostIdResponse createPost(Long userId, CreatePostRequest request) {
        User writer = userRepository.findByIdOrThrow(userId);
        Post post = Post.of(
                writer.getId(),
                request.title(),
                request.content(),
                request.postImageUrls()
        );
        Long postId = postRepository.save(post).getId();

        PostView postView = PostView.from(postId);
        postViewRepository.save(postView);

        return new PostIdResponse(postId);
    }

    public void updatePost(Long userId, Long postId, UpdatePostRequest request) {
        User writer = userRepository.findByIdOrThrow(userId);
        Post post = postRepository.findByIdOrThrow(postId);
        if (!post.getUserId().equals(writer.getId())) {
            throw new ForbiddenException(
                    ErrorCode.ACCESS_DENIED,
                    "접근 권한이 없습니다."
            );
        }

        post.update(request.title(), request.content(), request.postImageUrls());
        postRepository.save(post);
    }

    public void deletePost(Long userId, Long postId) {
        User writer = userRepository.findByIdOrThrow(userId);
        Post post = postRepository.findByIdOrThrow(postId);
        if (!post.getUserId().equals(writer.getId())) {
            throw new ForbiddenException(
                    ErrorCode.ACCESS_DENIED,
                    "접근 권한이 없습니다."
            );
        }

        postRepository.deleteById(post.getId());
    }
}
