package com.portfoliopro.service;

import com.portfoliopro.dto.PortfolioSummaryDTO;
import com.portfoliopro.entity.Holding;
import com.portfoliopro.entity.VirtualWallet;
import com.portfoliopro.repository.HoldingRepository;
import com.portfoliopro.repository.VirtualWalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PortfolioService {

    private final HoldingRepository holdingRepository;
    private final VirtualWalletRepository walletRepository;

    public PortfolioService(
            HoldingRepository holdingRepository,
            VirtualWalletRepository walletRepository) {

        this.holdingRepository = holdingRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional(readOnly = true)
    public PortfolioSummaryDTO getPortfolioSummary(Long portfolioId) {

        List<Holding> holdings =
                holdingRepository.findByPortfolioId(portfolioId);

        BigDecimal investedAmount = BigDecimal.ZERO;
        BigDecimal currentHoldingsValue = BigDecimal.ZERO;
        BigDecimal todaysProfitLoss = BigDecimal.ZERO;

        for (Holding holding : holdings) {

            int quantity = holding.getQuantity();

            BigDecimal avgPrice =
                    holding.getAvgPurchasePrice();

            BigDecimal currentPrice =
                    holding.getStock().getCurrentPrice();

            BigDecimal previousPrice =
                    holding.getStock().getPreviousPrice();

            // Total amount originally invested
            BigDecimal investedValue =
                    avgPrice.multiply(
                            BigDecimal.valueOf(quantity)
                    );

            // Current market value
            BigDecimal currentValue =
                    currentPrice.multiply(
                            BigDecimal.valueOf(quantity)
                    );

            // Today's P&L
            BigDecimal dailyChange =
                    currentPrice.subtract(previousPrice);

            BigDecimal dailyProfitLoss =
                    dailyChange.multiply(
                            BigDecimal.valueOf(quantity)
                    );

            investedAmount =
                    investedAmount.add(investedValue);

            currentHoldingsValue =
                    currentHoldingsValue.add(currentValue);

            todaysProfitLoss =
                    todaysProfitLoss.add(dailyProfitLoss);
        }

        VirtualWallet wallet =
                walletRepository.findByUserId(1L)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Virtual wallet not found"
                                ));

        BigDecimal availableBalance =
                wallet.getBalance();

        BigDecimal totalPortfolio =
                currentHoldingsValue.add(availableBalance);

        return new PortfolioSummaryDTO(
                totalPortfolio,
                investedAmount,
                availableBalance,
                todaysProfitLoss
        );
    }
}