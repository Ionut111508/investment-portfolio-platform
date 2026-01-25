package com.portfolio_service.service;

import com.portfolio_service.dto.CreatePortfolioRequest;
import com.portfolio_service.dto.PortfolioDTO;
import com.portfolio_service.entity.Portfolio;
import com.portfolio_service.repository.PortfolioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;

    public PortfolioService(PortfolioRepository portfolioRepository) {
        this.portfolioRepository = portfolioRepository;
    }

    public String sayHello() {
        return " Hello from service";
    }

    public List<PortfolioDTO> getPortfolios() {
        return portfolioRepository.findAll()
                .stream()
                .map(portfolio -> PortfolioDTO.builder()
                        .id(portfolio.getId())
                        .owner(portfolio.getOwner())
                        .totalValue(portfolio.getTotalValue())
                        .createdAt(portfolio.getCreatedAt())
                        .currency(portfolio.getCurrency())
                        .build())
                .collect(Collectors.toList());
    }

    public Portfolio createPortfolio(CreatePortfolioRequest request) {
        Portfolio portfolio = Portfolio.builder()
                .owner(request.getOwner())
                .totalValue(request.getTotalValue())
                .createdAt(LocalDateTime.now())
                .currency(request.getCurrency())
                .build();
        return portfolioRepository.save(portfolio);
    }
}
