package com.jian.community.presentation.interceptor;

import com.jian.community.application.service.SessionManager;
import com.jian.community.domain.model.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
        UserSession session = sessionManager.getValidSession(httpRequest);

        // 요청에 사용자 정보 주입
        httpRequest.setAttribute("userId", session.getUserId());

        return true;
    }
}
