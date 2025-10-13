package com.jian.community.presentation.config;

import com.jian.community.infrastructure.bucket4j.RateLimitFilter;
import com.jian.community.presentation.interceptor.SessionValidationInterceptor;
import io.github.bucket4j.Bucket;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final SessionValidationInterceptor sessionValidationInterceptor;
    private final Bucket bucket;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionValidationInterceptor)
                .addPathPatterns("/**") // 전체 API 대상
                .excludePathPatterns(
                        "/users/**/availability", // 이메일, 닉네임 중복 검사 API
                        "/files/profile-images" // 프로필 이미지 업로드 API
                );
    }

    @Bean
    public RateLimitFilter rateLimitFilter() {
        return new RateLimitFilter(bucket);
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> registerRateLimitFilter(RateLimitFilter rateLimitFilter) {
        FilterRegistrationBean<RateLimitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(rateLimitFilter);
        registrationBean.addUrlPatterns("/*"); // 전체 API 대상
        registrationBean.setOrder(1); // 최우선으로 처리

        return registrationBean;
    }
}
