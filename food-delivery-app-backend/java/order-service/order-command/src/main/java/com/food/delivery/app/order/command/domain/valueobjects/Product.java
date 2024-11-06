package com.food.delivery.app.order.command.domain.valueobjects;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class Product {

    private UUID productId;
    private BigDecimal price;
    private boolean available;
}
