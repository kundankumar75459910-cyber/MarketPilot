package com.portfoliopro.dto;

import java.math.BigDecimal;

public class OrderResponseDTO {

    private String message;
    private String symbol;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private BigDecimal remainingBalance;

    public OrderResponseDTO(
            String message,
            String symbol,
            int quantity,
            BigDecimal price,
            BigDecimal totalAmount,
            BigDecimal remainingBalance) {

        this.message = message;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = totalAmount;
        this.remainingBalance = remainingBalance;
    }

    public String getMessage() {
        return message;
    }

    public String getSymbol() {
        return symbol;
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

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }
}