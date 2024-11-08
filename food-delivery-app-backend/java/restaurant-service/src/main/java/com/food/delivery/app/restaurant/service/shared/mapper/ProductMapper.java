package com.food.delivery.app.restaurant.service.shared.mapper;

import com.food.delivery.app.restaurant.service.domain.entity.Product;
import com.food.delivery.app.restaurant.service.shared.repository.product.ProductEntity;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public List<ProductEntity> productsToEntities(List<Product> products, RestaurantEntity restaurant) {
        return products.stream().map(product -> productToEntity(product, restaurant)).toList();
    }

    public List<Product> productsFromEntities(List<ProductEntity> products) {
        return products.stream().map(this::productFromEntity).toList();
    }

    public ProductEntity productToEntity(Product product, RestaurantEntity restaurant) {
        return ProductEntity.builder()
                .productId(product.getProductId())
                .restaurant(restaurant)
                .name(product.getName())
                .price(product.getPrice())
                .available(product.isAvailable())
                .description(product.getDescription())
                .build();
    }

    public Product productFromEntity(ProductEntity product) {
        return Product.builder()
                .restaurantId(product.getRestaurant().getRestaurantId())
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .available(product.isAvailable())
                .description(product.getDescription())
                .build();
    }
}
