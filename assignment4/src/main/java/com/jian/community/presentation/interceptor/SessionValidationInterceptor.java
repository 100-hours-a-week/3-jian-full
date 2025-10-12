package com.jian.community.presentation.interceptor;

import com.jian.community.application.service.SessionManager;
import com.jian.community.domain.constant.ErrorCode;
import com.jian.community.domain.exception.UnauthorizedException;
import com.jian.community.domain.model.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class SessionValidationInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;

    @Override
    public boolean preHandle(
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse,
            Object handler
    ) {
        // 로그인 & 회원가입 요청 통과
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (requestURI.equals("/sessions") || requestURI.equals("/users")) {
            if (method.equals("POST")) {
                return true;
            }
        }

        // 세션 검증
        UserSession session = sessionManager.getSession(httpRequest);
        if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
            sessionManager.expireSession(httpRequest);
            throw new UnauthorizedException(
                    ErrorCode.AUTHENTICATION_REQUIRED,
                    "세션이 만료되었거나 존재하지 않습니다."
            );
        }

        // 세션 연장
        sessionManager.extendSession(httpRequest);
        return true;
    }
}
