package com.food.delivery.app.restaurant.service.features.create_restaurant.v1.endpoints;

import com.food.delivery.app.common.annotation.role.verification.CheckRoles;
import com.food.delivery.app.restaurant.service.domain.entity.Restaurant;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.command.CreateRestaurantCommandHandler;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantCommand;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantResponse;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.mapper.RestaurantCommandMapper;
import com.food.delivery.app.restaurant.service.shared.util.SecurityContextUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
public class CreateRestaurantEndpoint {

    private final CreateRestaurantCommandHandler createRestaurantCommandHandler;
    private final RestaurantCommandMapper restaurantCommandMapper;
    private final SecurityContextUtil securityContextUtil;

    public CreateRestaurantEndpoint(CreateRestaurantCommandHandler createRestaurantCommandHandler,
                                    RestaurantCommandMapper restaurantCommandMapper,
                                    SecurityContextUtil securityContextUtil) {
        this.createRestaurantCommandHandler = createRestaurantCommandHandler;
        this.restaurantCommandMapper = restaurantCommandMapper;
        this.securityContextUtil = securityContextUtil;
    }

    @PostMapping
    @CheckRoles(roles = {"restaurant-manager"})
    public ResponseEntity<CreateRestaurantResponse> createRestaurant(@RequestBody @Validated CreateRestaurantCommand createRestaurantCommand) {
        UUID ownerId = securityContextUtil.getUUIDFromSecurityContext();
        createRestaurantCommand.setOwnerId(ownerId);

        Restaurant restaurant = createRestaurantCommandHandler.handleCreateRestaurantCommand(createRestaurantCommand);
        return ResponseEntity.ok(restaurantCommandMapper.restaurantToResponse(restaurant));
    }
}
