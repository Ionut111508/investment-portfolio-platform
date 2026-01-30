package com.portfolio_service.exception;

import com.portfolio_service.error.ErrorCode;

public class PortfolioNotFoundException extends BusinessException {

    public PortfolioNotFoundException(Long id) {
        super(
                "Portfolio with id " + id + " not found",
                ErrorCode.RESOURCE_NOT_FOUND
        );
    }
}

