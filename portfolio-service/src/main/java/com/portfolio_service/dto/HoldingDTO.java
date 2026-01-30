package com.portfolio_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class HoldingDTO {
    private String ticker;
    private String name;
    private BigDecimal totalShares;
    private BigDecimal averagePrice;
    private BigDecimal totalValue;
    private String currency;
}
