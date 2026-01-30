package com.portfolio_service.error;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

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
        ApiErrorResponse response = new ApiErrorResponse();
        response.errorCode = errorCode.getCode();
        response.message = message;
        response.status = errorCode.getHttpStatus();
        response.path = request.getRequestURI();
        response.timestamp = LocalDateTime.now();
        return response;
    }
}
