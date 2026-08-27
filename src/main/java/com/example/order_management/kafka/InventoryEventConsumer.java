package com.example.order_management.kafka;

import com.example.order_management.event.InventoryEvent;
import com.example.order_management.service.OrderService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class InventoryEventConsumer {

    private final OrderService orderService;

    private final ObjectMapper objectMapper;

    public InventoryEventConsumer(
            OrderService orderService,
            ObjectMapper objectMapper) {

        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "inventory-events",
            groupId = "order-service"
    )
    public void consume(String message) {

        try {

            System.out.println(
                    "Inventory event received: "
                            + message
            );

            InventoryEvent event =
                    objectMapper.readValue(
                            message,
                            InventoryEvent.class
                    );

            if (event.isSuccess()) {

                orderService.updateOrderStatus(
                        event.getOrderId(),
                        "CONFIRMED"
                );

                orderService.createShipping(
                        event.getOrderId()
                );

            } else {

                orderService.updateOrderStatus(
                        event.getOrderId(),
                        "REJECTED"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}