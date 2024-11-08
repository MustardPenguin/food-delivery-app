package com.food.delivery.app.restaurant.service.shared.repository.product;

import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM restaurant.products p WHERE p.restaurant_id = :id")
    List<ProductEntity> findAllByRestaurantId(UUID id);
}
