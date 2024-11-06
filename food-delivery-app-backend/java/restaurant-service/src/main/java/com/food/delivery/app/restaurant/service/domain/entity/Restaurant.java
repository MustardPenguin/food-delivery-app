package com.food.delivery.app.restaurant.service.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Restaurant {

    private UUID restaurantId;
    private String name;
    private String address;
    private UUID ownerId;
    private boolean active;
    private List<Product> products;
}
