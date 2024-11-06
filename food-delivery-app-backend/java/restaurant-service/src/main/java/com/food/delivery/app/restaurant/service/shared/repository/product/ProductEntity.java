package com.food.delivery.app.restaurant.service.shared.repository.product;

import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class ProductEntity {

    @Id
    private UUID productId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean available;
}
