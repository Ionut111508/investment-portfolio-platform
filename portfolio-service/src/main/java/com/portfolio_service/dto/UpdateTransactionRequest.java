package com.portfolio_service.dto;

import com.portfolio_service.enums.TransactionAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTransactionRequest {
    private TransactionAction action;
    private String ticker;
    private String name;
    private BigDecimal numberOfShares;
    private BigDecimal pricePerShare;
    private BigDecimal total;
    private String currency;
    private BigDecimal conversionFee;
    private LocalDateTime time;
}
