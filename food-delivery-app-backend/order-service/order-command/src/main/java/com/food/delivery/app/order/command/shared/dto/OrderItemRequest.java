package com.food.delivery.app.order.command.shared.dto;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderItemRequest {

    private UUID productId;
    private BigDecimal price;
    private int quantity;
}
