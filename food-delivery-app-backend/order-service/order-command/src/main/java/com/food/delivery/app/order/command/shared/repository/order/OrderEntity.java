package com.food.delivery.app.order.command.shared.repository.order;

import com.food.delivery.app.order.command.domain.valueobjects.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    List<OrderItemEntity> orderItems;
    private String address;
    private LocalDateTime orderedAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
