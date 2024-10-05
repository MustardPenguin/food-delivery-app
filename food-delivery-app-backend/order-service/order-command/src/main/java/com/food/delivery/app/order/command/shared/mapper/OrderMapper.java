package com.food.delivery.app.order.command.shared.mapper;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.order.command.shared.repository.order.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderMapper {

    public OrderEntity orderToEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .restaurantId(order.getRestaurantId())
                .orderStatus(order.getOrderStatus())
                .customerId(order.getCustomerId())
                .orderedAt(order.getOrderedAt())
                .address(order.getAddress())
                .orderId(UUID.randomUUID())
                .build();

        orderEntity.setOrderItems(orderItemsToEntities(order.getOrderItems(), orderEntity));

        return orderEntity;
    }

    public Order orderFromEntity(OrderEntity orderEntity) {
        return Order.builder()
                .orderItems(orderItemsFromEntities(orderEntity.getOrderItems()))
                .restaurantId(orderEntity.getRestaurantId())
                .orderStatus(orderEntity.getOrderStatus())
                .customerId(orderEntity.getCustomerId())
                .orderedAt(orderEntity.getOrderedAt())
                .orderId(orderEntity.getOrderId())
                .address(orderEntity.getAddress())
                .build();
    }

    private List<OrderItemEntity> orderItemsToEntities(List<OrderItem> orderItems, OrderEntity orderEntity) {
        return orderItems.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .orderItemId(orderItem.getOrderItemId())
                        .productId(orderItem.getProductId())
                        .quantity(orderItem.getQuantity())
                        .order(orderEntity)
                        .build())
                .toList();
    }

    private List<OrderItem> orderItemsFromEntities(List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.stream()
                .map(orderItemEntity -> OrderItem.builder()
                        .orderItemId(orderItemEntity.getOrderItemId())
                        .productId(orderItemEntity.getProductId())
                        .quantity(orderItemEntity.getQuantity())
                        .build())
                .toList();
    }
}
