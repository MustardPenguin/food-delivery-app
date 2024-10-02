package com.food.delivery.app.command.shared.mapper;

import com.food.delivery.app.command.shared.entity.OrderItem;
import com.food.delivery.app.command.shared.repository.order.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderItemEntity orderItemToEntity(OrderItem orderItem) {
        return OrderItemEntity.builder()
                .orderItemId(orderItem.getOrderItemId())
                .productId(orderItem.getProductId())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem orderItemFromEntity(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .orderItemId(orderItemEntity.getOrderItemId())
                .productId(orderItemEntity.getProductId())
                .quantity(orderItemEntity.getQuantity())
                .build();
    }
}
