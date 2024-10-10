package com.food.delivery.app.restaurant.service.features.create_restaurant.v1.endpoints;

import com.food.delivery.app.common.annotation.role.verification.CheckRoles;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurant")
public class CreateRestaurantEndpoint {

    @PostMapping
    @CheckRoles(roles = {"restaurant-manager"})
    public void createRestaurant() {

    }
}
