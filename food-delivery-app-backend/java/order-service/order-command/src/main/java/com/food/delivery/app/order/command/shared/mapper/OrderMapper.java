package com.food.delivery.app.order.command.shared.mapper;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.event.payload.OrderItemEventPayload;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.shared.dto.OrderItemRequest;
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
                .errorMessage(order.getErrorMessage())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .customerId(order.getCustomerId())
                .orderedAt(order.getOrderedAt())
                .paymentId(order.getPaymentId())
                .walletId(order.getWalletId())
                .address(order.getAddress())
                .orderId(order.getOrderId())
                .build();

        orderEntity.setOrderItems(orderItemsToEntities(order.getOrderItems(), orderEntity));

        return orderEntity;
    }

    public Order orderFromEntity(OrderEntity orderEntity) {
        return Order.builder()
                .orderItems(orderItemsFromEntities(orderEntity.getOrderItems()))
                .restaurantId(orderEntity.getRestaurantId())
                .errorMessage(orderEntity.getErrorMessage())
                .orderStatus(orderEntity.getOrderStatus())
                .totalPrice(orderEntity.getTotalPrice())
                .customerId(orderEntity.getCustomerId())
                .orderedAt(orderEntity.getOrderedAt())
                .paymentId(orderEntity.getPaymentId())
                .walletId(orderEntity.getWalletId())
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
                        .price(orderItem.getPrice())
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
                        .price(orderItemEntity.getPrice())
                        .build())
                .toList();
    }

    public List<OrderItem> orderItemFromRequest(List<OrderItemRequest> orderItems) {
        return orderItems.stream().map(orderItem ->
                OrderItem.builder()
                        .productId(orderItem.getProductId())
                        .quantity(orderItem.getQuantity())
                        .price(orderItem.getPrice())
                        .build()).toList();
    }

    public OrderEventPayload orderToEventPayload(Order order) {
        List<OrderItemEventPayload> orderItems = order.getOrderItems().stream().map(item -> OrderItemEventPayload.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build()).toList();
        return OrderEventPayload.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .walletId(order.getWalletId())
                .orderItems(orderItems)
                .address(order.getAddress())
                .orderedAt(order.getOrderedAt())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
