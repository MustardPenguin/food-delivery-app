package com.food.delivery.app.restaurant.service.shared.mapper;

import com.food.delivery.app.common.domain.entity.RestaurantProduct;
import com.food.delivery.app.restaurant.service.domain.entity.Restaurant;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantMapper {

    public RestaurantEntity restaurantToEntity(Restaurant restaurant) {
        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
                .restaurantId(restaurant.getRestaurantId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .ownerId(restaurant.getOwnerId())
                .build();

        restaurantEntity.setRestaurantProducts(restaurantProductsToEntities(restaurant.getRestaurantProducts(), restaurantEntity));

        return restaurantEntity;
    }

    public Restaurant restaurantFromEntity(RestaurantEntity restaurant) {
        return Restaurant.builder()
                .restaurantId(restaurant.getRestaurantId())
                .restaurantName(restaurant.getRestaurantName())
                .address(restaurant.getAddress())
                .ownerId(restaurant.getOwnerId())
                .restaurantProducts(restaurantProductsFromEntities(restaurant.getRestaurantProducts()))
                .build();
    }

    public List<RestaurantProductEntity> restaurantProductsToEntities(List<RestaurantProduct> products, RestaurantEntity restaurant) {
        return products.stream().map(product ->
                RestaurantProductEntity.builder()
                        .productId(product.getProductId())
                        .restaurant(restaurant)
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .available(product.isAvailable())
                        .build()).toList();
    }

    public List<RestaurantProduct> restaurantProductsFromEntities(List<RestaurantProductEntity> products) {
        return products.stream().map(product ->
                RestaurantProduct.builder()
                        .restaurantId(product.getRestaurant().getRestaurantId())
                        .productId(product.getProductId())
                        .productName(product.getProductName())
                        .price(product.getPrice())
                        .available(product.isAvailable())
                        .build()).toList();
    }
}
