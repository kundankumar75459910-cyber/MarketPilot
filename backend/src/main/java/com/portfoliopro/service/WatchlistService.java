package com.portfoliopro.service;
import com.portfoliopro.dto.WatchlistDTO;
import com.portfoliopro.entity.Stock;
import com.portfoliopro.entity.User;
import com.portfoliopro.entity.Watchlist;
import com.portfoliopro.repository.StockRepository;
import com.portfoliopro.repository.UserRepository;
import com.portfoliopro.repository.WatchlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    public WatchlistService(
            WatchlistRepository watchlistRepository,
            UserRepository userRepository,
            StockRepository stockRepository) {

        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional(readOnly = true)
    public List<WatchlistDTO> getWatchlistByUser(Long userId) {

        List<Watchlist> watchlists =
                watchlistRepository.findByUserId(userId);

        return watchlists.stream()
                .map(this::convertToDTO)
                .toList();
    }
    @Transactional
    public void addToWatchlist(Long userId, Long stockId) {

        if (watchlistRepository
                .existsByUserIdAndStockId(userId, stockId)) {

            throw new IllegalArgumentException(
                    "Stock is already in your watchlist");
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Stock stock = stockRepository
                .findById(stockId)
                .orElseThrow(() ->
                        new RuntimeException("Stock not found"));

        Watchlist watchlist = new Watchlist();

        watchlist.setUser(user);
        watchlist.setStock(stock);

        watchlistRepository.save(watchlist);
    }

    @Transactional
    public void removeFromWatchlist(
            Long userId,
            Long stockId) {

        if (!watchlistRepository
                .existsByUserIdAndStockId(userId, stockId)) {

            throw new RuntimeException(
                    "Stock is not in your watchlist");
        }

        watchlistRepository
                .deleteByUserIdAndStockId(userId, stockId);
    }
    private WatchlistDTO convertToDTO(Watchlist watchlist) {

        return new WatchlistDTO(
                watchlist.getId(),
                watchlist.getStock().getId(),
                watchlist.getStock().getSymbol(),
                watchlist.getStock().getCompanyName(),
                watchlist.getStock().getSector(),
                watchlist.getStock().getCurrentPrice(),
                watchlist.getStock().getPreviousPrice(),
                watchlist.getAddedAt()
        );
    }
}