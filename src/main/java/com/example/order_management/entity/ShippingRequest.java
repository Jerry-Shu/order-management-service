package com.example.order_management.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRequest {

    private Long orderId;

    private Long productId;

    private Integer quantity;
}