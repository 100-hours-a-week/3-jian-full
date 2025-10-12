package com.jian.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostView extends MinimalEntity {

    @Getter
    private Long postId;
    private AtomicInteger count = new AtomicInteger(0);

    public static PostView from(Long postId) {
        PostView postView = new PostView();
        postView.postId = postId;
        return postView;
    }

    public int increase(){
        return count.incrementAndGet();
    }

    public int getCount(){
        return count.get();
    }
}
