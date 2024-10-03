package com.food.delivery.app.command.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    private UUID orderItemId;
    private UUID productId;
    private int quantity;
}
