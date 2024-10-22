package com.food.delivery.app.order.command.features.create_order.v1.mapper;

import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.order.command.shared.mapper.OrderMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderCommandMapper {

    private final OrderMapper orderMapper;

    public OrderCommandMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public Order commandToOrder(CreateOrderCommand createOrderCommand, UUID customerId) {
        return Order.builder()
                .restaurantId(createOrderCommand.getRestaurantId())
                .customerId(customerId)
                .orderItems(orderMapper.orderItemFromRequest(createOrderCommand.getOrderItems()))
                .address(createOrderCommand.getAddress())
                .build();
    }

    public CreateOrderResponse orderToResponse(Order order) {
        return CreateOrderResponse.builder()
                .restaurantId(order.getRestaurantId())
                .orderStatus(order.getOrderStatus())
                .customerId(order.getCustomerId())
                .totalPrice(order.getTotalPrice())
                .orderItems(order.getOrderItems())
                .orderedAt(order.getOrderedAt())
                .address(order.getAddress())
                .orderId(order.getOrderId())
                .build();
    }
}
