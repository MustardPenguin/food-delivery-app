package com.food.delivery.app.restaurant.service.features.create_product.v1.command;

import com.food.delivery.app.common.domain.entity.Product;
import com.food.delivery.app.restaurant.service.domain.entity.Restaurant;
import com.food.delivery.app.restaurant.service.features.create_product.v1.dto.CreateProductCommand;
import com.food.delivery.app.restaurant.service.features.create_product.v1.mapper.ProductCommandMapper;
import com.food.delivery.app.restaurant.service.shared.exceptions.RestaurantException;
import com.food.delivery.app.restaurant.service.shared.mapper.ProductMapper;
import com.food.delivery.app.restaurant.service.shared.mapper.RestaurantMapper;
import com.food.delivery.app.restaurant.service.shared.repository.product.ProductEntity;
import com.food.delivery.app.restaurant.service.shared.repository.product.ProductJpaRepository;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreateProductCommandHandler {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final ProductCommandMapper productCommandMapper;
    private final ProductMapper productMapper;

    public CreateProductCommandHandler(RestaurantJpaRepository restaurantJpaRepository,
                                       ProductJpaRepository productJpaRepository,
                                       ProductCommandMapper productCommandMapper,
                                       ProductMapper productMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.productJpaRepository = productJpaRepository;
        this.productCommandMapper = productCommandMapper;
        this.productMapper = productMapper;
    }

    @Transactional
    public Product handleCreateProductCommand(CreateProductCommand command, UUID restaurantId, UUID managerId) {

        Optional<RestaurantEntity> optionalRestaurant = restaurantJpaRepository.findByRestaurantId(restaurantId);

        if(optionalRestaurant.isEmpty()) {
            throw new RestaurantException("Restaurant of id " + restaurantId + " does not exist!");
        }
        RestaurantEntity restaurant = optionalRestaurant.get();
        if(!restaurant.getOwnerId().equals(managerId)) {
            throw new RestaurantException("You must own the restaurant to add products!");
        }

        Product product = productCommandMapper.commandToProduct(command);
        product.setProductId(UUID.randomUUID());
        ProductEntity productEntity = productJpaRepository.save(productMapper.productToEntity(product, restaurant));

        return productMapper.productFromEntity(productEntity);
    }
}
