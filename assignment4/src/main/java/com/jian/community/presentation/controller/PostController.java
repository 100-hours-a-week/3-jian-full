package com.jian.community.presentation.controller;

import com.jian.community.application.service.PostLikeService;
import com.jian.community.application.service.PostQueryService;
import com.jian.community.application.service.PostService;
import com.jian.community.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Post", description = "게시글 관련 API")
@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostQueryService postQueryService;
    private final PostService postService;
    private final PostLikeService postLikeService;

    @Operation(
            summary = "게시글 목록 조회",
            description = "커서 기반 페이징 방식으로 게시글을 최대 10개씩 조회합니다. "
                    + "cursor를 입력하지 않으면 첫 페이지를 반환합니다."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<PostResponse> getPosts(
            @RequestParam(required = false) LocalDateTime cursor
    ) {
        return postQueryService.getPosts(cursor);
    }

    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글 상세 정보와 댓글 목록의 첫 페이지(최대 10개)가 함께 반환됩니다."
    )
    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDetailResponse getPostDetail(
            @PathVariable Long postId
    ) {
        return postQueryService.getPostDetail(postId);
    }

    @Operation(summary = "게시글 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostIdResponse createPost(
            @Valid @RequestBody CreatePostRequest request,
            @RequestAttribute Long userId
    ) {
        return postService.createPost(userId, request);
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePost(
            @PathVariable Long postId,
            @Valid @RequestBody UpdatePostRequest request,
            @RequestAttribute Long userId
    ) {
        postService.updatePost(userId, postId, request);
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postService.deletePost(userId, postId);
    }

    @Operation(summary = "게시글 좋아요")
    @PostMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.CREATED)
    public void createPostLike(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postLikeService.createPostLike(postId, userId);
    }

    @Operation(summary = "게시글 좋아요 취소")
    @DeleteMapping("/{postId}/likes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePostLike(
            @PathVariable Long postId,
            @RequestAttribute Long userId
    ) {
        postLikeService.deletePostLike(postId, userId);
    }
}
