package com.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePortfolioRequest {
    private String owner;
    private BigDecimal totalValue;
    private String currency;
}
