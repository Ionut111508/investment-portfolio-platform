package com.portfolio_service.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // 4xx - client errors
    INVALID_REQUEST_PARAMETER("INVALID_REQUEST_PARAMETER", 400),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", 404),
    VALIDATION_ERROR("VALIDATION_ERROR", 400),

    // 5xx - server errors
    INTERNAL_ERROR("INTERNAL_ERROR", 500);

    private final String code;
    private final int httpStatus;

    ErrorCode(String code, int httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

}
