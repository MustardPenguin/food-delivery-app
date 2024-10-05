package com.food.delivery.app.order.command.features.create_order.v1.dtos;

import com.food.delivery.app.order.command.domain.entity.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class CreateOrderResponse {

    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    List<OrderItem> orderItems;
    private String address;
    private LocalDateTime orderedAt;
}
