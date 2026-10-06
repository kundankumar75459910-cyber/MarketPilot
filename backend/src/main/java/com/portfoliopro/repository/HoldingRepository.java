package com.portfoliopro.repository;

import com.portfoliopro.entity.Holding;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA generates the implementation at runtime.
 * Method names like findByUsername are parsed into SQL automatically.
 */
@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {
    List<Holding> findByPortfolioId(Long portfolioId);

    Optional<Holding> findByPortfolioIdAndStockId(Long portfolioId, Long stockId);
}
