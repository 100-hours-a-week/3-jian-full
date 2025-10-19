package com.jian.community.presentation.config;

import com.jian.community.application.exception.ErrorCode;
import com.jian.community.application.exception.ErrorMessage;
import com.jian.community.presentation.exception.ErrorResponse;
import com.jian.community.presentation.exception.FieldErrorResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "아무말 대잔치 REST API 명세서",
                version = "v1.0.0",
                description = "카테부 풀스택 과정 5주차 과제",
                contact = @Contact(name = "jian.lee(이가연)", email = "qaz102912@gmail.com")
        )
)
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer globalFallbackErrorResponses() {
        return openApi -> openApi.getPaths().values().forEach(path ->
                path.readOperations().forEach(op -> {
                    op.getResponses()
                            .addApiResponse("400",
                                    buildFieldErrorResponse(ErrorCode.INVALID_USER_INPUT, "요청 파라미터가 유효하지 않습니다.", "field"))
                            .addApiResponse("401",
                                    buildErrorResponse(ErrorCode.AUTHENTICATION_REQUIRED, ErrorMessage.INVALID_SESSION))
                            .addApiResponse("403",
                                    buildErrorResponse(ErrorCode.ACCESS_DENIED, ErrorMessage.ACCESS_DENIED))
                            .addApiResponse("404",
                                    buildErrorResponse(ErrorCode.RESOURCE_NOT_FOUND, "요청한 자원을 찾을 수 없습니다."))
                            .addApiResponse("429",
                                    buildErrorResponse(ErrorCode.TOO_MANY_REQUESTS, ErrorMessage.TOO_MANY_REQUESTS))
                            .addApiResponse("500",
                                    buildErrorResponse( ErrorCode.INTERNAL_SERVER_ERROR, ErrorMessage.INTERNAL_SERVER_ERROR));
                })
        );
    }

    private ApiResponse buildErrorResponse(ErrorCode code, String message) {
        MediaType example = new MediaType().example(new ErrorResponse(code, message));
        Content content = new Content().addMediaType(APPLICATION_JSON_VALUE, example);

        return new ApiResponse().content(content);
    }

    private ApiResponse buildFieldErrorResponse(ErrorCode code, String message, String field) {
        MediaType example = new MediaType().example(new FieldErrorResponse(code, message, field));
        Content content = new Content().addMediaType(APPLICATION_JSON_VALUE, example);

        return new ApiResponse().content(content);
    }
}
