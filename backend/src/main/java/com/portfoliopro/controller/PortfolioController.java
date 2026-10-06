package com.portfoliopro.controller;

import com.portfoliopro.dto.PortfolioSummaryDTO;
import com.portfoliopro.service.PortfolioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolio")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(
            PortfolioService portfolioService) {

        this.portfolioService = portfolioService;
    }

    @GetMapping("/summary/{portfolioId}")
    public PortfolioSummaryDTO getPortfolioSummary(
            @PathVariable Long portfolioId) {

        return portfolioService.getPortfolioSummary(portfolioId);
    }
}