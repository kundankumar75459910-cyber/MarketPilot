package com.portfoliopro.service;

import com.portfoliopro.dto.HoldingDTO;
import com.portfoliopro.entity.Holding;
import com.portfoliopro.repository.HoldingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HoldingService {

    private final HoldingRepository holdingRepository;

    public HoldingService(HoldingRepository holdingRepository) {
        this.holdingRepository = holdingRepository;
    }

    @Transactional(readOnly = true)
    public List<HoldingDTO> getHoldingsByPortfolio(Long portfolioId) {

        List<Holding> holdings =
                holdingRepository.findByPortfolioId(portfolioId);

        return holdings.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private HoldingDTO convertToDTO(Holding holding) {

        BigDecimal avgPurchasePrice =
                holding.getAvgPurchasePrice();

        BigDecimal currentPrice =
                holding.getStock().getCurrentPrice();

        int quantity = holding.getQuantity();

        BigDecimal investedValue =
                avgPurchasePrice.multiply(
                        BigDecimal.valueOf(quantity)
                );

        BigDecimal currentValue =
                currentPrice.multiply(
                        BigDecimal.valueOf(quantity)
                );

        BigDecimal profitLoss =
                currentValue.subtract(investedValue);

        return new HoldingDTO(
                holding.getId(),
                holding.getStock().getSymbol(),
                holding.getStock().getCompanyName(),
                quantity,
                avgPurchasePrice,
                currentPrice,
                investedValue,
                currentValue,
                profitLoss
        );
    }

    public Holding getHoldingById(Long id) {

        return holdingRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Holding not found with id: " + id
                        ));
    }
}