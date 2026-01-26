package com.portfolio_service.service;

import com.portfolio_service.entity.Transaction;
import com.portfolio_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioCalculationService {

    public final TransactionRepository transactionRepository;

    public PortfolioCalculationService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal calculateCash(Long portfolioId) {
        List<Transaction> transactions = transactionRepository.findByPortfolioId(portfolioId);

        BigDecimal cash = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if ("Deposit".equals(t.getAction()) || "Withdrawal".equals(t.getAction())) {
                cash = cash.add(t.getTotal());
                cash = cash.subtract(t.getConversionFee());
            }
        }

        return cash;
    }

    public BigDecimal calculateInvestmentsValue(Long portfolioId) {
        List<Transaction> transactions = transactionRepository.findByPortfolioId(portfolioId);

        BigDecimal investments = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if ("Market buy".equals(t.getAction()) || "Market sell".equals(t.getAction())) {
                investments = investments.add(t.getTotal());
                investments = investments.add(t.getConversionFee());
            }
        }

        return investments;
    }
}
