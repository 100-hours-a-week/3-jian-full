package com.jian.community.application.service;

import com.jian.community.application.exception.ErrorCode;
import com.jian.community.application.exception.UnauthorizedException;
import com.jian.community.domain.model.UserSession;
import com.jian.community.domain.repository.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

// transactional
@Service
@AllArgsConstructor
public class SessionManager {

    private static final Duration SESSION_TTL = Duration.ofHours(2);

    private final UserSessionRepository userSessionRepository;

    public void createSession(Long userId, HttpServletResponse httpResponse) {
        String sessionId = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        UserSession session = UserSession.of(
                sessionId,
                userId,
                now,
                now.plus(SESSION_TTL)
        );
        userSessionRepository.save(session);

        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        httpResponse.addCookie(cookie);
    }

    public UserSession getValidSession(HttpServletRequest httpRequest) {
        UserSession session = getSession(httpRequest);

        if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
            expireSession(session.getSessionId());

            throw new UnauthorizedException(
                    ErrorCode.AUTHENTICATION_REQUIRED,
                    "세션이 만료되었거나 존재하지 않습니다."
            );
        }

        return extendSession(session);
    }

    public UserSession getSession(HttpServletRequest httpRequest) {
        Optional<UserSession> session = getSessionId(httpRequest)
                .flatMap(userSessionRepository::findBySessionId);

        return session.orElseThrow(() -> new UnauthorizedException(
                ErrorCode.AUTHENTICATION_REQUIRED,
                "세션이 만료되었거나 존재하지 않습니다."
        ));
    }

    public void expireSession(HttpServletRequest httpRequest) {
        getSessionId(httpRequest)
                .ifPresent(this::expireSession);
    }

    public void expireSession(String sessionId) {
        userSessionRepository.deleteBySessionId(sessionId);
    }

    private UserSession extendSession(UserSession session) {
        LocalDateTime now = LocalDateTime.now();

        session.extendSession(now, SESSION_TTL);
        return userSessionRepository.save(session);
    }

    private Optional<String> getSessionId(HttpServletRequest httpRequest) {
        return Optional.ofNullable(httpRequest.getCookies()).stream()
                .flatMap(Arrays::stream)
                .filter(cookie -> "JSESSIONID".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);
    }
}
