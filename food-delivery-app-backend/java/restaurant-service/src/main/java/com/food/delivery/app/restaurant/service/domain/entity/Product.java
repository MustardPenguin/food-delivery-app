package com.food.delivery.app.restaurant.service.domain.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private UUID restaurantId;
    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;
}