package com.jian.community.application.service;

import com.jian.community.domain.model.PostView;
import com.jian.community.domain.repository.PostViewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostViewService {

    private final PostViewRepository postViewRepository;

    public PostView increaseAndGet(Long postId) {
        PostView postView = postViewRepository.findByPostIdOrThrow(postId);
        postView.increase();
        return postViewRepository.save(postView);
    }
}
