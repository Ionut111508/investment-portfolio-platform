package com.portfolio_service.controller;

import com.portfolio_service.dto.HoldingDTO;
import com.portfolio_service.service.HoldingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolios/{portfolioId}/holdings")
@RequiredArgsConstructor
public class HoldingsController {

    private final HoldingsService holdingsService;

    @GetMapping
    public List<HoldingDTO> getHoldings(@PathVariable Long portfolioId) {
        return holdingsService.getHoldings(portfolioId);
    }
}
