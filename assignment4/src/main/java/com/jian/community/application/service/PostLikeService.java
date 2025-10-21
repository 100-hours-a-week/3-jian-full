package com.jian.community.application.service;

import com.jian.community.global.exception.ErrorMessage;
import com.jian.community.application.exception.ResourceNotFoundException;
import com.jian.community.domain.exception.UnauthorizedWriterException;
import com.jian.community.domain.model.Post;
import com.jian.community.domain.model.PostLike;
import com.jian.community.domain.model.User;
import com.jian.community.domain.repository.PostLikeRepository;
import com.jian.community.domain.repository.PostRepository;
import com.jian.community.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPostLike(Long postId, Long userId) {
        Post post = postRepository.findByIdOrThrow(postId);
        User user = userRepository.findByIdOrThrow(userId);

        PostLike postLike = PostLike.of(post.getId(), user.getId());
        postLikeRepository.save(postLike);
    }

    public void deletePostLike(Long postId, Long userId) {
        Post post = postRepository.findByIdOrThrow(postId);
        User user = userRepository.findByIdOrThrow(userId);

        postLikeRepository.findByPostIdAndUserId(postId, userId)
                .ifPresent((postLike -> {
                    validateCommandPermission(post, user, postLike);
                    postLikeRepository.deleteByPostIdAndUserId(post.getId(), user.getId());
                }));
    }

    private void validateCommandPermission(Post post, User user, PostLike postLike) {
        if (!postLike.isBelongsTo(post)) {
            throw new ResourceNotFoundException(ErrorMessage.POST_NOT_EXISTS);
        }
        if (!postLike.isLikedBy(user)) {
            throw new UnauthorizedWriterException(ErrorMessage.UNAUTHORIZED_POST_LIKE_USER);
        }
    }
}
