package com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateRestaurantResponse {

    private UUID restaurantId;
    private String restaurantName;
    private String address;
}
