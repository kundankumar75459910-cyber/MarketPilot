package com.portfoliopro.controller;
import com.portfoliopro.dto.OrderRequestDTO;
import com.portfoliopro.dto.OrderResponseDTO;
import com.portfoliopro.service.OrderService;
import org.springframework.web.bind.annotation.*;
import com.portfoliopro.dto.OrderDTO;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/buy")
    public OrderResponseDTO buy(
            @RequestBody OrderRequestDTO request) {

        return orderService.buy(request);
    }

    @PostMapping("/sell")
    public OrderResponseDTO sell(
            @RequestBody OrderRequestDTO request) {

        return orderService.sell(request);
    }
    @GetMapping("/user/{userId}")
    public List<OrderDTO> getOrdersByUser(
            @PathVariable Long userId) {

        return orderService.getOrdersByUser(userId);
    }
}