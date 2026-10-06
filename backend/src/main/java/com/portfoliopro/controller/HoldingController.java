package com.portfoliopro.controller;

import com.portfoliopro.dto.HoldingDTO;
import com.portfoliopro.entity.Holding;
import com.portfoliopro.service.HoldingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
@CrossOrigin(origins = "http://localhost:4200")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @GetMapping("/portfolio/{portfolioId}")
    public List<HoldingDTO> getHoldingsByPortfolio(
            @PathVariable Long portfolioId) {

        return holdingService.getHoldingsByPortfolio(portfolioId);
    }

    @GetMapping("/{id}")
    public Holding getHoldingById(@PathVariable Long id) {

        return holdingService.getHoldingById(id);
    }
}