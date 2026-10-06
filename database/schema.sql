-- PortfolioPro: SIMULATED stock trading (educational use only, virtual money)
-- Requires MySQL 8+
DROP DATABASE IF EXISTS portfoliopro;
CREATE DATABASE portfoliopro CHARACTER SET utf8mb4;
USE portfoliopro;

-- 1. USERS: one row per registered student
CREATE TABLE users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,          -- BCrypt hash, never plain text
    full_name     VARCHAR(100),
    created_at    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. VIRTUAL_WALLET: virtual cash. One-to-One with users
CREATE TABLE virtual_wallet (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  BIGINT NOT NULL UNIQUE,
    balance  DECIMAL(15,2) NOT NULL DEFAULT 100000.00,
    CONSTRAINT chk_balance CHECK (balance >= 0),
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 3. PORTFOLIOS: one portfolio per user (One-to-One for the college version)
CREATE TABLE portfolios (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT NOT NULL UNIQUE,
    name       VARCHAR(100) NOT NULL DEFAULT 'My Portfolio',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_portfolio_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 4. STOCKS: SAMPLE companies with SAMPLE fundamentals (fictional data)
CREATE TABLE stocks (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol         VARCHAR(10)  NOT NULL UNIQUE,
    company_name   VARCHAR(100) NOT NULL,
    sector         VARCHAR(50),
    current_price  DECIMAL(12,2) NOT NULL,
    previous_price DECIMAL(12,2) NOT NULL,
    market_cap     DECIMAL(18,2),                 -- sample value
    pe_ratio       DECIMAL(8,2),                  -- sample value
    eps            DECIMAL(10,2),                 -- sample value
    revenue        DECIMAL(18,2)                  -- sample value
);

-- 5. PRICE_HISTORY: daily simulated closing prices (feeds SMA, volatility, charts)
CREATE TABLE price_history (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    stock_id    BIGINT NOT NULL,
    price_date  DATE NOT NULL,
    close_price DECIMAL(12,2) NOT NULL,
    UNIQUE KEY uq_stock_date (stock_id, price_date),
    CONSTRAINT fk_history_stock FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

-- 6. HOLDINGS: what a portfolio currently owns. Many holdings per portfolio
CREATE TABLE holdings (
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    portfolio_id       BIGINT NOT NULL,
    stock_id           BIGINT NOT NULL,
    quantity           INT NOT NULL,
    avg_purchase_price DECIMAL(12,2) NOT NULL,
    UNIQUE KEY uq_portfolio_stock (portfolio_id, stock_id),
    CONSTRAINT chk_qty CHECK (quantity > 0),
    CONSTRAINT fk_holding_portfolio FOREIGN KEY (portfolio_id) REFERENCES portfolios(id),
    CONSTRAINT fk_holding_stock     FOREIGN KEY (stock_id)     REFERENCES stocks(id)
);

-- 7. ORDERS: every order the user places (even rejected ones)
CREATE TABLE orders (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT NOT NULL,
    stock_id         BIGINT NOT NULL,
    order_type       VARCHAR(4) NOT NULL,
    quantity         INT NOT NULL,
    price            DECIMAL(12,2) NOT NULL,
    status           VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    rejection_reason VARCHAR(200),
    created_at       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_order_qty CHECK (quantity > 0),
    CONSTRAINT fk_order_user  FOREIGN KEY (user_id)  REFERENCES users(id),
    CONSTRAINT fk_order_stock FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

-- 8. TRANSACTIONS: created only when an order is EXECUTED (One-to-One with orders)
CREATE TABLE transactions (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id         BIGINT NOT NULL UNIQUE,
    user_id          BIGINT NOT NULL,
    stock_id         BIGINT NOT NULL,
    transaction_type VARCHAR(4) NOT NULL,
    quantity         INT NOT NULL,
    price            DECIMAL(12,2) NOT NULL,
    total_value      DECIMAL(15,2) NOT NULL,
    executed_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_txn_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_txn_user  FOREIGN KEY (user_id)  REFERENCES users(id),
    CONSTRAINT fk_txn_stock FOREIGN KEY (stock_id) REFERENCES stocks(id)
);

-- 9. WATCHLISTS: stocks a user follows (Many-to-Many users <-> stocks)
CREATE TABLE watchlists (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id  BIGINT NOT NULL,
    stock_id BIGINT NOT NULL,
    added_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_stock (user_id, stock_id),
    CONSTRAINT fk_watch_user  FOREIGN KEY (user_id)  REFERENCES users(id),
    CONSTRAINT fk_watch_stock FOREIGN KEY (stock_id) REFERENCES stocks(id)
);
