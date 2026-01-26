package com.portfolio_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;
    private LocalDateTime time;
    private String ticker;
    private String name;
    private BigDecimal numberOfShares;
    private BigDecimal pricePerShare;
    private String currency;
    private BigDecimal total;
    private BigDecimal conversionFee;
    private Long portfolioId;
}
