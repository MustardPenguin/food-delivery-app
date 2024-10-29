package com.food.delivery.app.restaurant.service.features.create_product.v1.mapper;

import com.food.delivery.app.restaurant.service.domain.entity.Product;
import com.food.delivery.app.restaurant.service.features.create_product.v1.dto.CreateProductCommand;
import org.springframework.stereotype.Component;

@Component
public class ProductCommandMapper {

    public Product commandToProduct(CreateProductCommand command) {
        return Product.builder()
                .name(command.getName())
                .price(command.getPrice())
                .available(command.isAvailable())
                .description(command.getDescription())
                .build();
    }
}
