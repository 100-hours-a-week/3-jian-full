package com.jian.community.presentation.controller;

import com.jian.community.application.service.PostService;
import com.jian.community.application.service.SessionManager;
import com.jian.community.presentation.dto.*;
import jakarta.servlet.http.HttpServletRequest;
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
    private final SessionManager sessionManager;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<PostResponse> getPosts(
            @RequestParam(required = false) LocalDateTime cursor
            // HttpServletRequest httpRequest
    ) {
        return postService.getPosts(cursor);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDetailResponse getPostDetail(
            @PathVariable Long postId
            // HttpServletRequest httpRequest
    ) {
        return postService.getPostDetail(postId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostIdResponse createPost(
            @Valid @RequestBody CreatePostRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = sessionManager.getSession(httpRequest).getUserId();
        return postService.createPost(userId, request);
    }

    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = sessionManager.getSession(httpRequest).getUserId();
        postService.updatePost(userId, postId, request);
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable Long postId,
            HttpServletRequest httpRequest
    ) {
        Long userId = sessionManager.getSession(httpRequest).getUserId();
        postService.deletePost(userId, postId);
    }
}
