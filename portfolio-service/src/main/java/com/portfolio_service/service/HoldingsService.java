package com.portfolio_service.service;

import com.portfolio_service.dto.HoldingDTO;
import com.portfolio_service.entity.Transaction;
import com.portfolio_service.enums.TransactionAction;
import com.portfolio_service.exception.PortfolioNotFoundException;
import com.portfolio_service.repository.PortfolioRepository;
import com.portfolio_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HoldingsService {

    private final TransactionRepository transactionRepository;
    private final PortfolioRepository portfolioRepository;

    @Cacheable(value = "holdingsCache", key = "#portfolioId")
    public List<HoldingDTO> getHoldings(Long portfolioId) {
        if (!portfolioRepository.existsById(portfolioId)) {
            throw new PortfolioNotFoundException(portfolioId);
        }

        List<Transaction> transactions =
                transactionRepository.findByPortfolioId(portfolioId);

        Map<String, List<Transaction>> groupedByTicker =
                transactions.stream()
                        .filter(t -> t.getTicker() != null)
                        .filter(t ->
                                t.getAction() == TransactionAction.MARKET_BUY ||
                                        t.getAction() == TransactionAction.MARKET_SELL
                        )
                        .collect(Collectors.groupingBy(Transaction::getTicker));

        List<HoldingDTO> holdings = new ArrayList<>();

        for (Map.Entry<String, List<Transaction>> entry : groupedByTicker.entrySet()) {

            String ticker = entry.getKey();
            List<Transaction> txs = entry.getValue();

            BigDecimal totalShares = BigDecimal.ZERO;
            BigDecimal totalInvested = BigDecimal.ZERO;
            String name = null;
            String currency = null;

            for (Transaction tx : txs) {
                name = tx.getName();
                currency = tx.getCurrency();

                if (tx.getAction() == TransactionAction.MARKET_BUY) {
                    totalShares = totalShares.add(tx.getNumberOfShares());
                    totalInvested = totalInvested.add(tx.getTotal());
                }

                if (tx.getAction() == TransactionAction.MARKET_SELL) {
                    totalShares = totalShares.subtract(tx.getNumberOfShares());
                }
            }

            if (totalShares.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal averagePrice =
                    totalInvested.divide(totalShares, 4, RoundingMode.HALF_UP);

            holdings.add(
                    HoldingDTO.builder()
                            .ticker(ticker)
                            .name(name)
                            .totalShares(totalShares)
                            .totalValue(totalInvested)
                            .averagePrice(averagePrice)
                            .currency(currency)
                            .build()
            );
        }

        return holdings;
    }
}
