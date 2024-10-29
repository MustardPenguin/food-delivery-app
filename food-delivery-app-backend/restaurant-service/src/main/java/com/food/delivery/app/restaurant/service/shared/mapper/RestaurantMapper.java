package com.food.delivery.app.restaurant.service.shared.mapper;

import com.food.delivery.app.restaurant.service.domain.entity.Restaurant;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {

    private final ProductMapper productMapper;

    public RestaurantMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public RestaurantEntity restaurantToEntity(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .ownerId(restaurant.getOwnerId())
                .active(restaurant.isActive())
                .build();

        restaurantEntity.setProducts(productMapper.productsToEntities(restaurant.getProducts(), restaurantEntity));

        return restaurantEntity;
    }

    public Restaurant restaurantFromEntity(RestaurantEntity restaurant) {
        return Restaurant.builder()
                .restaurantId(restaurant.getRestaurantId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .ownerId(restaurant.getOwnerId())
                .active(restaurant.isActive())
                .products(productMapper.productsFromEntities(restaurant.getProducts()))
                .build();
    }
}
