package com.food.delivery.app.order.command.domain.entity;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private UUID orderItemId;
    private UUID productId;
    private int quantity;
}
