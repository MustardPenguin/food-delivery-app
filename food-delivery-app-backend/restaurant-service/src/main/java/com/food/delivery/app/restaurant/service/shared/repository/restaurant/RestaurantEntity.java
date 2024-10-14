package com.food.delivery.app.restaurant.service.shared.repository.restaurant;

import com.food.delivery.app.restaurant.service.shared.repository.product.ProductEntity;
import jakarta.persistence.*;
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
    private String name;
    private String address;
    private UUID ownerId;
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private List<ProductEntity> products;
}
