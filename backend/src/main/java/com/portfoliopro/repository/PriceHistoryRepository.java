package com.portfoliopro.repository;

import com.portfoliopro.entity.PriceHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA generates the implementation at runtime.
 * Method names like findByUsername are parsed into SQL automatically.
 */
@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    /** Oldest to newest: used for SMA, volatility and charts. */
    List<PriceHistory> findByStockIdOrderByPriceDateAsc(Long stockId);

    /** Newest first, limited to 30 rows. */
    List<PriceHistory> findTop30ByStockIdOrderByPriceDateDesc(Long stockId);
}
