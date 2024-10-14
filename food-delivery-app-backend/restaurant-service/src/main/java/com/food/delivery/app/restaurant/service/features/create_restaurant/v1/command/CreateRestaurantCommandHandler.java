package com.food.delivery.app.restaurant.service.features.create_restaurant.v1.command;

import com.food.delivery.app.restaurant.service.domain.entity.Restaurant;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantCommand;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantResponse;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.mapper.RestaurantCommandMapper;
import com.food.delivery.app.restaurant.service.shared.mapper.RestaurantMapper;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateRestaurantCommandHandler {

    private final RestaurantCommandMapper restaurantCommandMapper;
    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantMapper restaurantMapper;

    public CreateRestaurantCommandHandler(RestaurantCommandMapper restaurantCommandMapper,
                                          RestaurantJpaRepository restaurantJpaRepository,
                                          RestaurantMapper restaurantMapper) {
        this.restaurantCommandMapper = restaurantCommandMapper;
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantMapper = restaurantMapper;
    }

    @Transactional
    public Restaurant handleCreateRestaurantCommand(CreateRestaurantCommand createRestaurantCommand) {
        Restaurant restaurant = restaurantCommandMapper.commandToRestaurant(createRestaurantCommand);
        restaurant.setRestaurantId(UUID.randomUUID());
        restaurant.setActive(false);

        restaurantJpaRepository.save(restaurantMapper.restaurantToEntity(restaurant));

        return restaurant;
    }
}
