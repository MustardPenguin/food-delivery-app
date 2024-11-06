package com.food.delivery.app.restaurant.service.features.create_product.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductCommand {

    @NotNull(message = "Name must not be null!")
    @NotBlank(message = "Name must not be blank!")
    private String name;
    private String description;
    @NotNull(message = "Price must not be blank!")
    private BigDecimal price;
    private boolean available;
}
