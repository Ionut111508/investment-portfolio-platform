package com.portfolio_service.exception;

import com.portfolio_service.error.ErrorCode;

public class GenericBusinessException extends BusinessException {
    public GenericBusinessException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}