package com.portfolio_service.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PortfolioValueDTO {
    private BigDecimal cashBalance;
    private BigDecimal investmentsValue;
    private BigDecimal totalValue;
}
