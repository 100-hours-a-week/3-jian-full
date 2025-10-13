package com.jian.community.presentation.controller;

import com.jian.community.application.service.CommentService;
import com.jian.community.application.service.PostLikeService;
import com.jian.community.application.service.PostService;
import com.jian.community.presentation.dto.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<PostResponse> getPosts(
            @RequestParam(required = false) LocalDateTime cursor
    ) {
        return postService.getPosts(cursor);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDetailResponse getPostDetail(
            @PathVariable Long postId
    ) {
        return postService.getPostDetail(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostIdResponse createPost(
            @Valid @RequestBody CreatePostRequest request,
            @RequestAttribute Long userId
    ) {
        return postService.createPost(userId, request);
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request,
            @RequestAttribute Long userId
    ) {
        postService.updatePost(userId, postId, request);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postService.deletePost(userId, postId);
    }

    @PostMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPostLike(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postLikeService.createPostLike(postId, userId);
    }

    @DeleteMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostLike(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postLikeService.deletePostLike(postId, userId);
    }

    @GetMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<CommentResponse> getComments(
            @PathVariable Long postId,
            @RequestParam(required = false) LocalDateTime cursor
    ) {
        return commentService.getComments(postId, cursor);
    }

    @PostMapping("/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse creatComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @RequestAttribute Long userId
    ) {
        return commentService.creatComment(postId, userId, request);
    }

    @PutMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @RequestAttribute Long userId
    ) {
        commentService.updateComment(postId, commentId, userId, request);
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestAttribute Long userId
    ) {
        commentService.deleteComment(postId, commentId, userId);
    }
}
