package com.portfolio_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private String action;
    private LocalDateTime time;
    private String ticker;
    private double shares;
    private double price;
    private double total;
    private String currency;
}
