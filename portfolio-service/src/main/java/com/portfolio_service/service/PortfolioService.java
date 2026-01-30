package com.portfolio_service.service;

import com.opencsv.CSVReader;
import com.portfolio_service.dto.CreatePortfolioRequest;
import com.portfolio_service.dto.PortfolioDTO;
import com.portfolio_service.entity.Portfolio;
import com.portfolio_service.entity.Transaction;
import com.portfolio_service.exception.PortfolioNotFoundException;
import com.portfolio_service.repository.PortfolioRepository;
import com.portfolio_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final PortfolioCalculationService portfolioCalculationService;


    public PortfolioService(PortfolioRepository portfolioRepository, TransactionService transactionService, TransactionRepository transactionRepository, PortfolioCalculationService portfolioCalculationService) {
        this.portfolioRepository = portfolioRepository;
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
        this.portfolioCalculationService = portfolioCalculationService;
    }

    public List<PortfolioDTO> getPortfolios() {
        return portfolioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PortfolioDTO getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException(id));
        return mapToDTO(portfolio);
    }

    public PortfolioDTO createPortfolio(CreatePortfolioRequest request) {
        Portfolio portfolio = Portfolio.builder()
                .owner(request.getOwner())
                .totalValue(request.getTotalValue())
                .currency(request.getCurrency())
                .createdAt(LocalDateTime.now())
                .build();
        return mapToDTO(portfolioRepository.save(portfolio));
    }

    public PortfolioDTO updatePortfolio(Long id, CreatePortfolioRequest request) {
        Portfolio existing = portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException(id));

        existing.setOwner(request.getOwner());
        existing.setTotalValue(request.getTotalValue());
        existing.setCurrency(request.getCurrency());

        return mapToDTO(portfolioRepository.save(existing));
    }

    public void deletePortfolio(Long id) {
        Portfolio existing = portfolioRepository.findById(id)
                .orElseThrow(() -> new PortfolioNotFoundException(id));
        portfolioRepository.delete(existing);
    }

    private PortfolioDTO mapToDTO(Portfolio portfolio) {
        BigDecimal cash = portfolioCalculationService.calculateCash(portfolio.getId());
        BigDecimal investments = portfolioCalculationService.calculateInvestmentsValue(portfolio.getId());
        BigDecimal total = cash.add(investments);

        return PortfolioDTO.builder()
                .id(portfolio.getId())
                .owner(portfolio.getOwner())
                .cashBalance(cash)
                .investmentsValue(investments)
                .totalValue(total)
                .currency(portfolio.getCurrency())
                .createdAt(portfolio.getCreatedAt())
                .build();
    }


    public void importCsvToPortfolio(MultipartFile file, Long portfolioId) throws Exception {
        CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()));
        String[] line;
        boolean firstLine = true;

        while ((line = reader.readNext()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }

            Transaction transaction = transactionService.mapCsvRowToTransaction(line, portfolioId);

            transactionRepository.save(transaction);
        }

        reader.close();
    }

}
