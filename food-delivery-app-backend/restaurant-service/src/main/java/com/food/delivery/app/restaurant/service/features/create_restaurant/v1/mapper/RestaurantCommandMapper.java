package com.food.delivery.app.restaurant.service.features.create_restaurant.v1.mapper;

import com.food.delivery.app.restaurant.service.domain.entity.Restaurant;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantCommand;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RestaurantCommandMapper {

    public Restaurant commandToRestaurant(CreateRestaurantCommand createRestaurantCommand) {
        return Restaurant.builder()
                .restaurantName(createRestaurantCommand.getRestaurantName())
                .restaurantProducts(new ArrayList<>())
                .address(createRestaurantCommand.getAddress())
                .ownerId(createRestaurantCommand.getOwnerId())
                .build();
    }

    public CreateRestaurantResponse restaurantToResponse(Restaurant restaurant) {
        return CreateRestaurantResponse.builder()
                .restaurantId(restaurant.getRestaurantId())
                .address(restaurant.getAddress())
                .restaurantName(restaurant.getRestaurantName())
                .build();
    }
}
