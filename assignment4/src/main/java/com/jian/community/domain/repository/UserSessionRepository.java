package com.jian.community.domain.repository;

import com.jian.community.domain.model.UserSession;

import java.util.Optional;

public interface UserSessionRepository {

    UserSession save(UserSession userSession);

    Optional<UserSession> findBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);
}
