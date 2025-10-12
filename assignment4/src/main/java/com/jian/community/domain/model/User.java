package com.jian.community.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User extends MinimalEntity {

    private String email;
    private String password;
    private String nickname;
    private String profileImageUrl;

    public static User of(String email, String password, String nickname, String profileImageUrl) {
        return new User(email, password, nickname, profileImageUrl);
    }

    public void update(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}
