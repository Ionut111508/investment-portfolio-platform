package com.portfolio_service.controller;

import com.portfolio_service.dto.CreatePortfolioRequest;
import com.portfolio_service.dto.PortfolioDTO;
import com.portfolio_service.dto.TransactionDTO;
import com.portfolio_service.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public List<PortfolioDTO> getPortfolios() {
        return portfolioService.getPortfolios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PortfolioDTO> getPortfolioById(@PathVariable Long id) {
        PortfolioDTO portfolio = portfolioService.getPortfolioById(id);
        return ResponseEntity.ok(portfolio);
    }

    @PostMapping
    public ResponseEntity<PortfolioDTO> createPortfolio(@RequestBody CreatePortfolioRequest request) {
        PortfolioDTO savedPortfolio = portfolioService.createPortfolio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPortfolio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PortfolioDTO> updatePortfolio(
            @PathVariable Long id,
            @RequestBody CreatePortfolioRequest request) {

        PortfolioDTO updatedPortfolio = portfolioService.updatePortfolio(id, request);
        return ResponseEntity.ok(updatedPortfolio);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/import")
    public ResponseEntity<String> importCsv(@PathVariable Long id,
                                            @RequestParam("file") MultipartFile file) {
        try {
            portfolioService.importCsvToPortfolio(file, id);
            return ResponseEntity.ok("CSV imported successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Import failed: " + e.getMessage());
        }
    }

}
