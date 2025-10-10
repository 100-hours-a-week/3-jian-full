package com.jian.community.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserSession extends MinimalEntity {

    private String sessionId;
    private Long userId;
    private LocalDateTime lastAccessedAt;
    private LocalDateTime expiresAt;

    public void extendSession(LocalDateTime lastAccessedAt, Duration duration) {
        this.lastAccessedAt = lastAccessedAt;
        this.expiresAt = lastAccessedAt.plus(duration);
    }
}
