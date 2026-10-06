package com.portfoliopro.repository;

import com.portfoliopro.entity.VirtualWallet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA generates the implementation at runtime.
 * Method names like findByUsername are parsed into SQL automatically.
 */
@Repository
public interface VirtualWalletRepository extends JpaRepository<VirtualWallet, Long> {
    Optional<VirtualWallet> findByUserId(Long userId);
}
