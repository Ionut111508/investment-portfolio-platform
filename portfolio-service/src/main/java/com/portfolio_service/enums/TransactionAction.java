package com.portfolio_service.enums;

import lombok.Getter;

@Getter
public enum TransactionAction {

    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    MARKET_BUY("Market buy"),
    MARKET_SELL("Market sell");

    private final String label;

    TransactionAction(String label) {
        this.label = label;
    }

    public static TransactionAction fromLabel(String label) {
        for (TransactionAction action : values()) {
            if (action.getLabel().equalsIgnoreCase(label)) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid TransactionAction: " + label);
    }
}
