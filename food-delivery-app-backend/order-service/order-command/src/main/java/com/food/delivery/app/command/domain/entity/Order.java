package com.food.delivery.app.command.domain.entity;


import com.food.delivery.app.command.domain.valueobjects.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Order {

    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;

    List<OrderItem> orderItems;
    private String address;
    private LocalDateTime orderedAt;
    private OrderStatus orderStatus;
}
