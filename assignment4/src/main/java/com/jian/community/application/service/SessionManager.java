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

    public void createSession(Long userId, HttpServletResponse response) {
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
        response.addCookie(cookie);
    }

    public void extendSession(HttpServletRequest request) {
        UserSession session = getSession(request);
        LocalDateTime now = LocalDateTime.now();

        session.extendSession(now, SESSION_TTL);
        userSessionRepository.save(session);
    }

    public void expireSession(HttpServletRequest request) {
        getSessionId(request)
                .ifPresent(userSessionRepository::deleteBySessionId);
    }

    public UserSession getSession(HttpServletRequest request) {
        Optional<UserSession> session = getSessionId(request)
                .flatMap(userSessionRepository::findBySessionId);

        return session.orElseThrow(() -> new UnauthorizedException(
                ErrorCode.AUTHENTICATION_REQUIRED,
                "세션이 만료되었거나 존재하지 않습니다."
        ));
    }

    private Optional<String> getSessionId(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "JSESSIONID".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue);
    }
}
