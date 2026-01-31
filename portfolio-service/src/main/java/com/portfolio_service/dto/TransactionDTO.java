package com.portfolio_service.dto;

import com.portfolio_service.entity.Transaction;
import com.portfolio_service.enums.TransactionAction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDTO {

    private Long id;
    private TransactionAction action;
    private LocalDateTime time;
    private String ticker;
    private String name;
    private BigDecimal numberOfShares;
    private BigDecimal pricePerShare;
    private BigDecimal total;
    private String currency;
    private BigDecimal conversionFee;

    public static TransactionDTO fromEntity(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .action(transaction.getAction())
                .time(transaction.getTime())
                .ticker(transaction.getTicker())
                .name(transaction.getName())
                .numberOfShares(transaction.getNumberOfShares())
                .pricePerShare(transaction.getPricePerShare())
                .total(transaction.getTotal())
                .currency(transaction.getCurrency())
                .conversionFee(transaction.getConversionFee())
                .build();
    }
}
