package com.portfolio_service.controller;

import com.portfolio_service.dto.CreatePortfolioRequest;
import com.portfolio_service.dto.PortfolioDTO;
import com.portfolio_service.entity.Portfolio;
import com.portfolio_service.service.PortfolioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping(path = "/api/v1/portfolios")
    public List<PortfolioDTO> getPortfolios(){
        return portfolioService.getPortfolios();
    }

    @PostMapping(path = "/api/v1/portfolios")
    public ResponseEntity<Portfolio> createPortfolio(@RequestBody CreatePortfolioRequest request){
        Portfolio savedPortfolio = portfolioService.createPortfolio(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPortfolio);
    }
}
