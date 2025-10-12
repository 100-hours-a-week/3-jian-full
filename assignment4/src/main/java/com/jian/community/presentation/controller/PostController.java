package com.jian.community.presentation.controller;

import com.jian.community.application.service.PostService;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.PostResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CursorResponse<PostResponse> getPosts(
            @RequestParam(required = false) LocalDateTime cursor,
            HttpServletRequest httpRequest
    ) {
        return postService.getPosts(cursor);
    }
}
