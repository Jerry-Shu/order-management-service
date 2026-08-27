package com.example.order_management.controller;

import com.example.order_management.entity.Order;
import com.example.order_management.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService) {

        this.orderService = orderService;
    }

    @PostMapping
    public Order createOrder(
            @RequestBody Order order) {

        return orderService.createOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {

        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrder(
            @PathVariable Long id) {

        return orderService.getOrder(id);
    }
}