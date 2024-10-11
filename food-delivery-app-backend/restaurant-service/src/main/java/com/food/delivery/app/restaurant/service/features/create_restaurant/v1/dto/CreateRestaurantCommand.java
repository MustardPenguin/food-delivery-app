package com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRestaurantCommand {

    @NotNull(message = "Name must not be blank!")
    @NotBlank(message = "Name must not be blank!")
    private String restaurantName;
    @NotNull(message = "Address must not be blank!")
    @NotBlank(message = "Address must not be blank!")
    private String address;
    private UUID ownerId;
}
