package com.example.order_management.kafka;

import com.example.order_management.event.OrderCreatedEvent;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class OrderEventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    public OrderEventProducer(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper) {

        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendOrderEvent(
            OrderCreatedEvent event) {

        try {

            String message =
                    objectMapper.writeValueAsString(event);

            kafkaTemplate.send(
                    "order-events",
                    message
            );

            System.out.println(
                    "Order event sent: " + message
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}