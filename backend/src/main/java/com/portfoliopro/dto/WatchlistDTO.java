package com.portfoliopro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WatchlistDTO {

    private Long id;
    private Long stockId;
    private String symbol;
    private String companyName;
    private String sector;
    private BigDecimal currentPrice;
    private BigDecimal previousPrice;
    private LocalDateTime addedAt;

    public WatchlistDTO() {
    }

    public WatchlistDTO(
            Long id,
            Long stockId,
            String symbol,
            String companyName,
            String sector,
            BigDecimal currentPrice,
            BigDecimal previousPrice,
            LocalDateTime addedAt) {

        this.id = id;
        this.stockId = stockId;
        this.symbol = symbol;
        this.companyName = companyName;
        this.sector = sector;
        this.currentPrice = currentPrice;
        this.previousPrice = previousPrice;
        this.addedAt = addedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getStockId() {
        return stockId;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSector() {
        return sector;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public BigDecimal getPreviousPrice() {
        return previousPrice;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }
}