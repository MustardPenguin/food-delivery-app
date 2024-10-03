package com.food.delivery.app.command.features.create_order.v1.mapper;

import com.food.delivery.app.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.command.domain.entity.Order;
import com.food.delivery.app.command.domain.valueobjects.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCommandMapper {
    public Order commandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .restaurantId(createOrderCommand.getRestaurantId())
                .customerId(createOrderCommand.getCustomerId())
                .orderItems(createOrderCommand.getOrderItems())
                .orderedAt(createOrderCommand.getOrderedAt())
                .address(createOrderCommand.getAddress())
                .orderStatus(OrderStatus.PENDING_PAYMENT)
                .orderId(UUID.randomUUID())
                .build();
    }
}
