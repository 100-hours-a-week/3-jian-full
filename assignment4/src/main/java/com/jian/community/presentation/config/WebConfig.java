package com.jian.community.presentation.config;

import com.jian.community.presentation.interceptor.SessionValidationInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionValidationInterceptor sessionValidationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionValidationInterceptor)
                .addPathPatterns("/**") // 전체 API 대상
                .excludePathPatterns(
                        "/users/**/availability", // 이메일, 닉네임 중복 검사 API
                        "/files/profile-images" // 프로필 이미지 업로드 API
                );
    }
}
