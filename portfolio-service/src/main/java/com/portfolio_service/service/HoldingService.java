package com.portfolio_service.service;

import com.portfolio_service.dto.HoldingDTO;
import com.portfolio_service.entity.Transaction;
import com.portfolio_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HoldingService {

    private final TransactionRepository transactionRepository;

    public List<HoldingDTO> getHoldingsByPortfolioId(Long portfolioId) {

        List<Transaction> transactions = transactionRepository.findByPortfolioId(portfolioId);

        Map<String, HoldingDTO> holdingsMap = new HashMap<>();

        for (Transaction t : transactions) {
            if (!t.getAction().equals("Market buy") && !t.getAction().equals("Market sell")) {
                continue;
            }

            holdingsMap.putIfAbsent(t.getTicker(),
                    HoldingDTO.builder()
                            .ticker(t.getTicker())
                            .name(t.getName())
                            .totalShares(BigDecimal.ZERO)
                            .averagePrice(BigDecimal.ZERO)
                            .currency(t.getCurrency())
                            .totalValue(BigDecimal.ZERO)
                            .build()
            );

            HoldingDTO h = holdingsMap.get(t.getTicker());

            BigDecimal signedShares = t.getAction().equals("Market buy") ? t.getNumberOfShares() : t.getNumberOfShares().negate();
            BigDecimal signedTotal = signedShares.multiply(t.getPricePerShare());

            // update total shares
            BigDecimal newTotalShares = h.getTotalShares().add(signedShares);

            // update average price
            BigDecimal newAveragePrice = BigDecimal.ZERO;
            if (newTotalShares.compareTo(BigDecimal.ZERO) != 0) {
                newAveragePrice = (h.getAveragePrice().multiply(h.getTotalShares()).add(signedTotal))
                        .divide(newTotalShares, 8, RoundingMode.HALF_UP);
            }

            // update total value
            BigDecimal newTotalValue = newTotalShares.multiply(newAveragePrice);

            // set values back
            h.setTotalShares(newTotalShares);
            h.setAveragePrice(newAveragePrice);
            h.setTotalValue(newTotalValue);
        }

        return new ArrayList<>(holdingsMap.values());
    }
}
