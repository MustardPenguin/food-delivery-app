package com.food.delivery.app.restaurant.service.shared.repository.restaurant;

import com.food.delivery.app.common.domain.entity.RestaurantProduct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
public class RestaurantEntity {

    @Id
    private UUID restaurantId;
    private String restaurantName;
    private String address;
    private UUID ownerId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private List<RestaurantProductEntity> restaurantProducts;
}
