package com.portfoliopro.controller;
import com.portfoliopro.dto.WatchlistDTO;
import com.portfoliopro.entity.Watchlist;
import com.portfoliopro.service.WatchlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@CrossOrigin(origins = "http://localhost:4200")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/user/{userId}")
    public List<WatchlistDTO> getWatchlist(@PathVariable Long userId) {
        return watchlistService.getWatchlistByUser(userId);
    }

    @PostMapping("/user/{userId}/stock/{stockId}")
    public String addToWatchlist(
            @PathVariable Long userId,
            @PathVariable Long stockId) {

        watchlistService.addToWatchlist(userId, stockId);

        return "Stock added to watchlist";
    }

    @DeleteMapping("/user/{userId}/stock/{stockId}")
    public String removeFromWatchlist(
            @PathVariable Long userId,
            @PathVariable Long stockId) {

        watchlistService.removeFromWatchlist(userId, stockId);

        return "Stock removed from watchlist";
    }
}