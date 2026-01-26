package com.portfolio_service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PortfolioDTO {
    private Long id;
    private String owner;
    private BigDecimal cashBalance;
    private BigDecimal investmentsValue;
    private BigDecimal totalValue;
    private String currency;
    private LocalDateTime createdAt;
}
