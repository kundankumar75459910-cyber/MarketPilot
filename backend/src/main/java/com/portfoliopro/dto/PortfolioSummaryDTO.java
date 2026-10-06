package com.portfoliopro.dto;

import java.math.BigDecimal;

public class PortfolioSummaryDTO {

    private BigDecimal totalPortfolio;
    private BigDecimal investedAmount;
    private BigDecimal availableBalance;
    private BigDecimal todaysProfitLoss;

    public PortfolioSummaryDTO() {
    }

    public PortfolioSummaryDTO(
            BigDecimal totalPortfolio,
            BigDecimal investedAmount,
            BigDecimal availableBalance,
            BigDecimal todaysProfitLoss) {

        this.totalPortfolio = totalPortfolio;
        this.investedAmount = investedAmount;
        this.availableBalance = availableBalance;
        this.todaysProfitLoss = todaysProfitLoss;
    }

    public BigDecimal getTotalPortfolio() {
        return totalPortfolio;
    }

    public BigDecimal getInvestedAmount() {
        return investedAmount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public BigDecimal getTodaysProfitLoss() {
        return todaysProfitLoss;
    }
}