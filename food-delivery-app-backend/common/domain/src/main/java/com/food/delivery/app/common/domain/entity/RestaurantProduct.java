package com.food.delivery.app.common.domain.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantProduct {

    private UUID restaurantId;
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private boolean available;
}