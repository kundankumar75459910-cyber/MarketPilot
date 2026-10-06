package com.portfoliopro.dto;

import java.math.BigDecimal;

public class HoldingDTO {

    private Long id;
    private String symbol;
    private String companyName;
    private int quantity;
    private BigDecimal avgPurchasePrice;
    private BigDecimal currentPrice;
    private BigDecimal investedValue;
    private BigDecimal currentValue;
    private BigDecimal profitLoss;

    public HoldingDTO() {
    }

    public HoldingDTO(
            Long id,
            String symbol,
            String companyName,
            int quantity,
            BigDecimal avgPurchasePrice,
            BigDecimal currentPrice,
            BigDecimal investedValue,
            BigDecimal currentValue,
            BigDecimal profitLoss) {

        this.id = id;
        this.symbol = symbol;
        this.companyName = companyName;
        this.quantity = quantity;
        this.avgPurchasePrice = avgPurchasePrice;
        this.currentPrice = currentPrice;
        this.investedValue = investedValue;
        this.currentValue = currentValue;
        this.profitLoss = profitLoss;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getAvgPurchasePrice() {
        return avgPurchasePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getInvestedValue() {
        return investedValue;
    }

    public BigDecimal getCurrentValue() {
        return currentValue;
    }

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }
}