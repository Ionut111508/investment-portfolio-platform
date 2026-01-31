package com.portfolio_service.error;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApiErrorResponse {

    private String errorCode;
    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

    public static ApiErrorResponse from(
            ErrorCode errorCode,
            String message,
            HttpServletRequest request
    ) {
        return ApiErrorResponse.builder()
                .errorCode(errorCode.getCode())
                .message(message)
                .status(errorCode.getHttpStatus())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
