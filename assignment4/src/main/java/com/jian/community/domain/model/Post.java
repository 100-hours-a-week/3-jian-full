package com.jian.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Post extends MinimalEntity {

    private Long userId;
    private String title;
    private String content;
    private List<String> postImageUrls;

    public static Post of(Long userId, String title, String content, List<String> postImageUrls) {
        return new Post(userId, title, content, postImageUrls);
    }

    public void update(String title, String content, List<String> postImageUrls) {
        this.title = title;
        this.content = content;
        this.postImageUrls = postImageUrls;
    }

    public boolean isWrittenBy(User writer){
        return userId.equals(writer.getId());
    }
}
