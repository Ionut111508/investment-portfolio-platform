package com.portfolio_service.controller;

import com.portfolio_service.dto.CreateTransactionRequest;
import com.portfolio_service.dto.TransactionDTO;
import com.portfolio_service.dto.UpdateTransactionRequest;
import com.portfolio_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios/{portfolioId}/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> addTransaction(
            @PathVariable Long portfolioId,
            @RequestBody @Valid CreateTransactionRequest request
    ) {
        TransactionDTO transaction = transactionService.addTransaction(portfolioId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transaction);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactions(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(transactionService.getTransactions(portfolioId));
    }

    @DeleteMapping("/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long portfolioId,
            @PathVariable Long transactionId
    ) {
        transactionService.deleteTransaction(portfolioId, transactionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody UpdateTransactionRequest request
    ) {
        TransactionDTO updated = transactionService.updateTransaction(transactionId, request);
        return ResponseEntity.ok(updated);
    }

}

