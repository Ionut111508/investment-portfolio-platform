package com.portfolio_service.service;

import com.portfolio_service.entity.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionService {

    protected Transaction mapCsvRowToTransaction(String[] row, Long portfolioId) {
        String action = row[0];
        LocalDateTime time = LocalDateTime.parse(row[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String ticker = row[3];
        String name = row[4];
        BigDecimal numberOfShares = row[7].isEmpty() ? BigDecimal.ZERO : new BigDecimal(row[7]);
        BigDecimal pricePerShare = row[8].isEmpty() ? BigDecimal.ZERO : new BigDecimal(row[8]);
        //BigDecimal exchangeRate = row[10].isEmpty() ? BigDecimal.ONE : new BigDecimal(row[10]);
        BigDecimal total = row[12].isEmpty() ? BigDecimal.ZERO : new BigDecimal(row[12]);
        String currency = row[13];

        BigDecimal conversionFee = row[14].isEmpty() ? BigDecimal.ZERO : new BigDecimal(row[14]);

        return Transaction.builder()
                .action(action)
                .time(time)
                .ticker(ticker)
                .name(name)
                .numberOfShares(numberOfShares)
                .pricePerShare(pricePerShare)
                .currency(currency)
                .total(total)
                .conversionFee(conversionFee)
                .portfolioId(portfolioId)
                .build();
    }
}
