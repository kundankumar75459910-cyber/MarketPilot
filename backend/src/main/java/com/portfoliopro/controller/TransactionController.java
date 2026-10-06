package com.portfoliopro.controller;

import com.portfoliopro.dto.TransactionDTO;
import com.portfoliopro.service.TransactionService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @GetMapping("/user/{userId}")
    public List<TransactionDTO> getTransactionsByUser(
            @PathVariable Long userId) {

        return transactionService
                .getTransactionsByUser(userId);
    }
}