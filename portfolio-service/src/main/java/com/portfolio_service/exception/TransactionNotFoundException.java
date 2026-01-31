package com.portfolio_service.exception;

import com.portfolio_service.error.ErrorCode;
import lombok.Getter;

@Getter
public class TransactionNotFoundException extends BusinessException {

    public TransactionNotFoundException(Long transactionId) {
        super("Transaction not found with id: " + transactionId, ErrorCode.RESOURCE_NOT_FOUND);
    }
}

