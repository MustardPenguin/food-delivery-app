package com.food.delivery.app.order.query.repository;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderItemEntity {

    @Id
    private UUID orderItemId;
    private UUID productId;
    private int quantity;
    private BigDecimal price;
}
