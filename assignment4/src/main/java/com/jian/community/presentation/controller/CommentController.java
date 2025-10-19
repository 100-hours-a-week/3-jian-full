package com.jian.community.presentation.controller;

import com.jian.community.application.service.CommentService;
import com.jian.community.presentation.dto.CommentResponse;
import com.jian.community.presentation.dto.CreateCommentRequest;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.UpdateCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Comment", description = "댓글 관련 API")
@RestController
@RequestMapping("/posts/{postId}/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "댓글 목록 조회",
            description = "커서 기반 페이징 방식으로 댓글을 최대 10개씩 조회합니다. "
                    + "cursor를 입력하지 않으면 첫 페이지를 반환합니다."
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<CommentResponse> getComments(
            @PathVariable Long postId,
            @RequestParam(required = false) LocalDateTime cursor
    ) {
        return commentService.getComments(postId, cursor);
    }

    @Operation(summary = "댓글 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse creatComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest request,
            @RequestAttribute Long userId
    ) {
        return commentService.creatComment(postId, userId, request);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody UpdateCommentRequest request,
            @RequestAttribute Long userId
    ) {
        commentService.updateComment(postId, commentId, userId, request);
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestAttribute Long userId
    ) {
        commentService.deleteComment(postId, commentId, userId);
    }
}
