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

    private User writer;
    private String title;
    private String content;
    private List<String> postImageUrls;

    public static Post of(User writer, String title, String content, List<String> postImageUrls) {
        return new Post(writer, title, content, postImageUrls);
    }
}
