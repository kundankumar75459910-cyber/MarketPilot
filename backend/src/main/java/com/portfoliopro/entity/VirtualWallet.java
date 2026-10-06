package com.portfoliopro.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Virtual (fake) cash of a user. Never real money.
 */
@Entity
@Table(name = "virtual_wallet")
public class VirtualWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    public VirtualWallet() {
        // JPA needs a no-argument constructor
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /** Add virtual money (used when a SELL executes). */
    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    /** Remove virtual money (used when a BUY executes). */
    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }
}
