package com.jian.community.application.service;

import com.jian.community.domain.dto.CursorPage;
import com.jian.community.domain.model.*;
import com.jian.community.domain.repository.*;
import com.jian.community.presentation.dto.CursorResponse;
import com.jian.community.presentation.dto.PostResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PostService {

    private final int PAGE_SIZE = 10;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostViewRepository postViewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CursorResponse<PostResponse> getPosts(LocalDateTime cursor) {
        CursorPage<Post> page = postRepository.findAllOrderByCreatedAtDesc(cursor, PAGE_SIZE);

        LocalDateTime nextCursor = page.hasNext() ? page.content().getLast().getCreateAt() : null;
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
                            post.getCreateAt(),
                            post.getUpdateAt()
                    );
                }).toList();

        return new CursorResponse<>(items, nextCursor, page.hasNext());
    }
}
