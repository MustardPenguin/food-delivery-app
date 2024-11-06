package com.food.delivery.app.restaurant.service.features.get_restaurant_products.v1.helper;

import com.food.delivery.app.common.proto.ProductServiceProto;
import com.food.delivery.app.restaurant.service.shared.repository.product.ProductEntity;
import com.food.delivery.app.restaurant.service.shared.repository.product.ProductJpaRepository;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetProductsHelper {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final ProductJpaRepository productJpaRepository;

    public GetProductsHelper(RestaurantJpaRepository restaurantJpaRepository,
                             ProductJpaRepository productJpaRepository) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    public List<ProductServiceProto.ProductResponse.Product> getProductsByRestaurantId(UUID restaurantId) {
        List<ProductEntity> products = productJpaRepository.findAllByRestaurantId(restaurantId);
        List<ProductServiceProto.ProductResponse.Product> productResponses =
                products.stream().map(this::entityToResponse).toList();
        return productResponses;
    }

    private ProductServiceProto.ProductResponse.Product entityToResponse(ProductEntity entity) {
        return ProductServiceProto.ProductResponse.Product.newBuilder()
                .setProductId(entity.getProductId().toString())
                .setPrice(entity.getPrice().doubleValue())
                .setAvailable(entity.isAvailable())
                .build();
    }
}
