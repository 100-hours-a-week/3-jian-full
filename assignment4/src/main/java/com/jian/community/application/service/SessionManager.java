package com.jian.community.application.service;

import com.jian.community.domain.constant.ErrorCode;
import com.jian.community.domain.exception.NotFoundException;
import com.jian.community.domain.model.UserSession;
import com.jian.community.domain.repository.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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
        UserSession userSession = new UserSession(
                sessionId,
                userId,
                now,
                now.plus(SESSION_TTL)
        );
        userSessionRepository.save(userSession);

        Cookie cookie = new Cookie("JSESSIONID", sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public UserSession getSession(HttpServletRequest request) {
        String sessionId = request.getHeader("JSESSIONID");
        return userSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new NotFoundException(
                        ErrorCode.SESSION_NOT_EXISTS,
                        "세션이 만료되었거나 존재하지 않습니다."
                ));
    }

    public void extendSession(HttpServletRequest request) {
        UserSession userSession = getSession(request);
        LocalDateTime now = LocalDateTime.now();

        userSession.extendSession(now, SESSION_TTL);
        userSessionRepository.save(userSession);
    }

    public void expireSession(HttpServletRequest request) {
        String sessionId = request.getHeader("JSESSIONID");

        if (userSessionRepository.existsBySessionId(sessionId)) {
            userSessionRepository.deleteBySessionId(sessionId);
        }
    }
}
