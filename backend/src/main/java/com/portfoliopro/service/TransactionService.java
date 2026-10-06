package com.portfoliopro.service;

import com.portfoliopro.dto.TransactionDTO;
import com.portfoliopro.entity.Transaction;
import com.portfoliopro.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(
            TransactionRepository transactionRepository) {

        this.transactionRepository = transactionRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getTransactionsByUser(Long userId) {

        List<Transaction> transactions =
                transactionRepository
                        .findByUserIdOrderByCreatedAtDesc(userId);

        return transactions.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private TransactionDTO convertToDTO(Transaction transaction) {

        return new TransactionDTO(
                transaction.getId(),
                transaction.getStock().getSymbol(),
                transaction.getTransactionType(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getTotalAmount(),
                transaction.getCreatedAt()
        );
    }
}