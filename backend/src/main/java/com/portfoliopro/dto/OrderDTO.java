package com.portfoliopro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {

    private Long id;
    private String symbol;
    private String orderType;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private String status;
    private String rejectionReason;
    private LocalDateTime createdAt;

    public OrderDTO() {
    }

    public OrderDTO(
            Long id,
            String symbol,
            String orderType,
            int quantity,
            BigDecimal price,
            BigDecimal totalAmount,
            String status,
            String rejectionReason,
            LocalDateTime createdAt) {

        this.id = id;
        this.symbol = symbol;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getOrderType() {
        return orderType;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}