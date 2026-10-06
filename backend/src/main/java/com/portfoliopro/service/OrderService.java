package com.portfoliopro.service;
import com.portfoliopro.dto.OrderDTO;
import com.portfoliopro.entity.Order;
import com.portfoliopro.entity.OrderStatus;
import com.portfoliopro.entity.OrderType;
import com.portfoliopro.repository.OrderRepository;
import com.portfoliopro.entity.Transaction;
import com.portfoliopro.repository.TransactionRepository;
import com.portfoliopro.dto.OrderRequestDTO;
import com.portfoliopro.dto.OrderResponseDTO;
import com.portfoliopro.entity.Holding;
import com.portfoliopro.entity.Portfolio;
import com.portfoliopro.entity.Stock;
import com.portfoliopro.entity.VirtualWallet;
import com.portfoliopro.repository.HoldingRepository;
import com.portfoliopro.repository.PortfolioRepository;
import com.portfoliopro.repository.StockRepository;
import com.portfoliopro.repository.VirtualWalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final TransactionRepository transactionRepository;
    private final OrderRepository orderRepository;
    private final PortfolioRepository portfolioRepository;
    private final StockRepository stockRepository;
    private final HoldingRepository holdingRepository;
    private final VirtualWalletRepository walletRepository;

    public OrderService(
            PortfolioRepository portfolioRepository,
            StockRepository stockRepository,
            HoldingRepository holdingRepository,
            VirtualWalletRepository walletRepository,
            TransactionRepository transactionRepository,
            OrderRepository orderRepository) {

        this.portfolioRepository = portfolioRepository;
        this.stockRepository = stockRepository;
        this.holdingRepository = holdingRepository;
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponseDTO buy(OrderRequestDTO request) {

        // 1. Validate quantity
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0");
        }

        // 2. Find portfolio
        Portfolio portfolio = portfolioRepository
                .findById(request.getPortfolioId())
                .orElseThrow(() ->
                        new RuntimeException("Portfolio not found"));

        // 3. Find stock
        Stock stock = stockRepository
                .findById(request.getStockId())
                .orElseThrow(() ->
                        new RuntimeException("Stock not found"));

        // 4. Find user's virtual wallet
        Long userId = portfolio.getUser().getId();

        VirtualWallet wallet = walletRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException("Virtual wallet not found"));

        // 5. Get current simulated price
        BigDecimal price = stock.getCurrentPrice();

        // 6. Calculate total amount
        BigDecimal totalAmount =
                price.multiply(BigDecimal.valueOf(request.getQuantity()));

        // 7. Check virtual balance
        if (wallet.getBalance().compareTo(totalAmount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient virtual balance");
        }

        // 8. Check whether holding already exists
        Holding holding = holdingRepository
                .findByPortfolioIdAndStockId(
                        portfolio.getId(),
                        stock.getId())
                .orElse(null);

        // 9. Create or update holding
        if (holding == null) {

            holding = new Holding();

            holding.setPortfolio(portfolio);
            holding.setStock(stock);
            holding.setQuantity(request.getQuantity());
            holding.setAvgPurchasePrice(price);

        } else {

            holding.addShares(
                    request.getQuantity(),
                    price);
        }

        holdingRepository.save(holding);

        // 10. Deduct virtual balance
        wallet.debit(totalAmount);

        walletRepository.save(wallet);
        Order order = new Order();

        order.setUser(portfolio.getUser());
        order.setStock(stock);
        order.setOrderType(OrderType.BUY);
        order.setQuantity(request.getQuantity());
        order.setPrice(price);
        order.setStatus(OrderStatus.EXECUTED);

        orderRepository.save(order);

// 11. Save transaction
        Transaction transaction = new Transaction();

        transaction.setUser(portfolio.getUser());
        transaction.setStock(stock);
        transaction.setTransactionType("BUY");
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(price);
        transaction.setTotalAmount(totalAmount);

        transactionRepository.save(transaction);

// 12. Return result
        return new OrderResponseDTO(
                "BUY order completed successfully",
                stock.getSymbol(),
                request.getQuantity(),
                price,
                totalAmount,
                wallet.getBalance()
        );
    }

    @Transactional
    public OrderResponseDTO sell(OrderRequestDTO request) {

        // 1. Validate quantity
        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than 0");
        }

        // 2. Find portfolio
        Portfolio portfolio = portfolioRepository
                .findById(request.getPortfolioId())
                .orElseThrow(() ->
                        new RuntimeException("Portfolio not found"));

        // 3. Find stock
        Stock stock = stockRepository
                .findById(request.getStockId())
                .orElseThrow(() ->
                        new RuntimeException("Stock not found"));

        // 4. Find existing holding
        Holding holding = holdingRepository
                .findByPortfolioIdAndStockId(
                        portfolio.getId(),
                        stock.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "You do not own this stock"));

        // 5. Check whether user has enough shares
        if (holding.getQuantity() < request.getQuantity()) {
            throw new IllegalArgumentException(
                    "Not enough shares to sell");
        }

        // 6. Get current simulated market price
        BigDecimal price = stock.getCurrentPrice();

        // 7. Calculate selling amount
        BigDecimal totalAmount =
                price.multiply(
                        BigDecimal.valueOf(request.getQuantity()));

        // 8. Reduce holding
        holding.removeShares(request.getQuantity());

        // 9. Delete holding if quantity becomes zero
        if (holding.getQuantity() == 0) {
            holdingRepository.delete(holding);
        } else {
            holdingRepository.save(holding);
        }

        // 10. Find virtual wallet
        Long userId = portfolio.getUser().getId();

        VirtualWallet wallet = walletRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Virtual wallet not found"));

        // 11. Add selling amount to virtual balance
        wallet.credit(totalAmount);

        walletRepository.save(wallet);
        Order order = new Order();

        order.setUser(portfolio.getUser());
        order.setStock(stock);
        order.setOrderType(OrderType.SELL);
        order.setQuantity(request.getQuantity());
        order.setPrice(price);
        order.setStatus(OrderStatus.EXECUTED);

        orderRepository.save(order);
        Transaction transaction = new Transaction();

        transaction.setUser(portfolio.getUser());
        transaction.setStock(stock);
        transaction.setTransactionType("SELL");
        transaction.setQuantity(request.getQuantity());
        transaction.setPrice(price);
        transaction.setTotalAmount(totalAmount);

        transactionRepository.save(transaction);

        // 12. Return response
        return new OrderResponseDTO(
                "SELL order completed successfully",
                stock.getSymbol(),
                request.getQuantity(),
                price,
                totalAmount,
                wallet.getBalance()
        );
    }
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUser(Long userId) {

        List<Order> orders =
                orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return orders.stream()
                .map(this::convertToDTO)
                .toList();
    }
    private OrderDTO convertToDTO(Order order) {

        BigDecimal totalAmount =
                order.getPrice()
                        .multiply(BigDecimal.valueOf(order.getQuantity()));

        return new OrderDTO(
                order.getId(),
                order.getStock().getSymbol(),
                order.getOrderType().name(),
                order.getQuantity(),
                order.getPrice(),
                totalAmount,
                order.getStatus().name(),
                order.getRejectionReason(),
                order.getCreatedAt()
        );
    }
}