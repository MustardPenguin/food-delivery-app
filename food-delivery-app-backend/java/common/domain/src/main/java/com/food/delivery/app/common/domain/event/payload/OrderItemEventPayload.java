package com.food.delivery.app.common.domain.event.payload;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class OrderItemEventPayload {

    private UUID orderItemId;
    private UUID productId;
    private BigDecimal price;
    private int quantity;
}
