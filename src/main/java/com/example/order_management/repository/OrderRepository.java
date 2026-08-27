package com.example.order_management.repository;

import com.example.order_management.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {

    private final Map<Long, Order> orders =
            new ConcurrentHashMap<>();

    private final AtomicLong idGenerator =
            new AtomicLong(1);

    public Order save(Order order) {

        if (order.getId() == null) {
            order.setId(
                    idGenerator.getAndIncrement()
            );
        }

        orders.put(
                order.getId(),
                order
        );

        return order;
    }

    public Optional<Order> findById(Long id) {

        return Optional.ofNullable(
                orders.get(id)
        );
    }

    public List<Order> findAll() {

        return new ArrayList<>(
                orders.values()
        );
    }
}