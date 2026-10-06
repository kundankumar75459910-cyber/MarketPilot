package com.portfoliopro.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Shares of ONE stock owned inside ONE portfolio (Many-to-One to both).
 */
@Entity
@Table(name = "holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIgnore
    private Portfolio portfolio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "avg_purchase_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal avgPurchasePrice;

    public Holding() {
        // JPA needs a no-argument constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAvgPurchasePrice() {
        return avgPurchasePrice;
    }

    public void setAvgPurchasePrice(BigDecimal avgPurchasePrice) {
        this.avgPurchasePrice = avgPurchasePrice;
    }

    /** Business rule: newAvg = (oldQty*oldAvg + buyQty*buyPrice) / (oldQty + buyQty). */
    public void addShares(int buyQty, BigDecimal buyPrice) {
        BigDecimal oldCost = avgPurchasePrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal newCost = buyPrice.multiply(BigDecimal.valueOf(buyQty));
        int newQty = quantity + buyQty;
        this.avgPurchasePrice = oldCost.add(newCost)
                .divide(BigDecimal.valueOf(newQty), 2, RoundingMode.HALF_UP);
        this.quantity = newQty;
    }

    /** Selling reduces quantity; average purchase price stays the same. */
    public void removeShares(int sellQty) {
        this.quantity = quantity - sellQty;
    }

    public BigDecimal getInvestedValue() {
        return avgPurchasePrice.multiply(BigDecimal.valueOf(quantity));
    }
}
