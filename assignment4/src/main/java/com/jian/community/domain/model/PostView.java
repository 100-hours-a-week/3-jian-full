package com.jian.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostView extends MinimalEntity {

    @Getter
    private Long postId;
    private AtomicLong count = new AtomicLong(0);

    public static PostView from(Long postId) {
        PostView postView = new PostView();
        postView.postId = postId;
        return postView;
    }

    public Long increase(){
        return count.incrementAndGet();
    }

    public Long getCount(){
        return count.get();
    }
}
