package com.example.order_management.service;

import com.example.order_management.entity.Order;
import com.example.order_management.event.OrderCreatedEvent;
import com.example.order_management.kafka.OrderEventProducer;
import com.example.order_management.repository.OrderRepository;
import org.springframework.stereotype.Service;

import com.example.order_management.entity.ShippingRequest;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderEventProducer orderEventProducer;

    private final RestTemplate restTemplate;

    public OrderService(
            OrderRepository orderRepository,
            OrderEventProducer orderEventProducer,
            RestTemplate restTemplate) {

        this.orderRepository = orderRepository;
        this.orderEventProducer = orderEventProducer;
        this.restTemplate = restTemplate;
    }

    public Order createOrder(Order order) {

        order.setId(null);

        order.setStatus("PENDING");

        Order savedOrder =
                orderRepository.save(order);

        OrderCreatedEvent event =
                new OrderCreatedEvent(
                        savedOrder.getId(),
                        savedOrder.getProductId(),
                        savedOrder.getQuantity()
                );

        orderEventProducer.sendOrderEvent(event);

        return savedOrder;
    }

    public Order getOrder(Long id) {

        return orderRepository
                .findById(id)
                .orElse(null);
    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }

    public void updateOrderStatus(
            Long orderId,
            String status) {

        Order order = getOrder(orderId);

        if (order != null) {

            order.setStatus(status);

            orderRepository.save(order);
        }
    }

    public void createShipping(Long orderId) {

        Order order = getOrder(orderId);

        if (order == null) {
            return;
        }

        ShippingRequest request =
                new ShippingRequest(
                        order.getId(),
                        order.getProductId(),
                        order.getQuantity()
                );

        String shippingUrl =
                "http://localhost:8083/api/shipments";

        restTemplate.postForObject(
                shippingUrl,
                request,
                String.class
        );

        System.out.println(
                "Shipping request sent for order: "
                        + orderId
        );
    }
}