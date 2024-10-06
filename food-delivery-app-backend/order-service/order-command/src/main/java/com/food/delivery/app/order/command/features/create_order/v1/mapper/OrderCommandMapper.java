package com.food.delivery.app.order.command.features.create_order.v1.mapper;

import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
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
                .build();
    }

    public CreateOrderResponse orderToResponse(Order order) {
        return CreateOrderResponse.builder()
                .restaurantId(order.getRestaurantId())
                .orderStatus(order.getOrderStatus())
                .customerId(order.getCustomerId())
                .orderItems(order.getOrderItems())
                .orderedAt(order.getOrderedAt())
                .address(order.getAddress())
                .orderId(order.getOrderId())
                .build();
    }
}
