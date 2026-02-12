package com.portfolio_service.service;

import com.portfolio_service.dto.CreateTransactionRequest;
import com.portfolio_service.dto.TransactionDTO;
import com.portfolio_service.dto.UpdateTransactionRequest;
import com.portfolio_service.entity.Transaction;
import com.portfolio_service.enums.TransactionAction;
import com.portfolio_service.error.ErrorCode;
import com.portfolio_service.exception.GenericBusinessException;
import com.portfolio_service.exception.PortfolioNotFoundException;
import com.portfolio_service.exception.TransactionNotFoundException;
import com.portfolio_service.repository.PortfolioRepository;
import com.portfolio_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final PortfolioRepository portfolioRepository;

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
                .action(TransactionAction.valueOf(action))
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

    @CacheEvict(value = "holdingsCache", key = "#portfolioId")
    public TransactionDTO addTransaction(Long portfolioId, CreateTransactionRequest request) {

        if (!portfolioRepository.existsById(portfolioId)) {
            throw new PortfolioNotFoundException(portfolioId);
        }

        BigDecimal numberOfShares =
                request.getNumberOfShares() != null ? request.getNumberOfShares() : BigDecimal.ZERO;

        BigDecimal pricePerShare =
                request.getPricePerShare() != null ? request.getPricePerShare() : BigDecimal.ZERO;

        BigDecimal conversionFee =
                request.getConversionFee() != null ? request.getConversionFee() : BigDecimal.ZERO;

        BigDecimal total = numberOfShares.multiply(pricePerShare);

        Transaction transaction = Transaction.builder()
                .portfolioId(portfolioId)
                .action(request.getAction())
                .ticker(request.getTicker())
                .name(request.getName())
                .numberOfShares(numberOfShares)
                .pricePerShare(pricePerShare)
                .total(total)
                .currency(request.getCurrency())
                .time(request.getTime() != null ? request.getTime() : LocalDateTime.now())
                .conversionFee(conversionFee)
                .build();

        return TransactionDTO.fromEntity(transactionRepository.save(transaction));
    }


    public List<TransactionDTO> getTransactions(Long portfolioId) {
        if (!portfolioRepository.existsById(portfolioId)) {
            throw new PortfolioNotFoundException(portfolioId);
        }
        return transactionRepository.findByPortfolioId(portfolioId)
                .stream()
                .sorted((t1, t2) -> t2.getTime().compareTo(t1.getTime()))
                .map(TransactionDTO::fromEntity)
                .toList();
    }

    @CacheEvict(value = "holdingsCache", key = "#portfolioId")
    public void deleteTransaction(Long portfolioId, Long transactionId) {
        if (!portfolioRepository.existsById(portfolioId)) {
            throw new PortfolioNotFoundException(portfolioId);
        }

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new GenericBusinessException("Transaction not found", ErrorCode.RESOURCE_NOT_FOUND));

        if (!transaction.getPortfolioId().equals(portfolioId)) {
            throw new GenericBusinessException("Transaction does not belong to portfolio", ErrorCode.INVALID_REQUEST_PARAMETER);
        }

        transactionRepository.delete(transaction);
    }

    @CacheEvict(value = "holdingsCache", key = "#portfolioId")
    public TransactionDTO updateTransaction(Long transactionId, UpdateTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));

        if (request.getAction() != null) transaction.setAction(request.getAction());
        if (request.getTicker() != null) transaction.setTicker(request.getTicker());
        if (request.getName() != null) transaction.setName(request.getName());
        if (request.getNumberOfShares() != null) transaction.setNumberOfShares(request.getNumberOfShares());
        if (request.getPricePerShare() != null) transaction.setPricePerShare(request.getPricePerShare());
        if (request.getTotal() != null) transaction.setTotal(request.getTotal());
        if (request.getCurrency() != null) transaction.setCurrency(request.getCurrency());
        if (request.getConversionFee() != null) transaction.setConversionFee(request.getConversionFee());
        if (request.getTime() != null) transaction.setTime(request.getTime());
        transaction = transactionRepository.save(transaction);

        return TransactionDTO.fromEntity(transaction);
    }

}
