package com.food.delivery.app.order.query.helper;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.order.query.repository.OrderEntity;
import com.food.delivery.app.order.query.repository.OrderItemEntity;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class OrderEventHelper {

    private final ObjectMapperUtil objectMapperUtil;

    public OrderEventHelper(ObjectMapperUtil objectMapperUtil) {
        this.objectMapperUtil = objectMapperUtil;
    }

    public OrderEntity payloadToOrderEntity(String payload) {
        OrderEventPayload order = objectMapperUtil.convertJsonToObject(payload, OrderEventPayload.class);
        List<OrderItemEntity> orderItems = order.getOrderItems().stream().map(item -> OrderItemEntity.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build()).toList();
        Long createdAt = order.getOrderedAt().toInstant(ZoneOffset.UTC).toEpochMilli();
        return OrderEntity.builder()
                .orderId(order.getOrderId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .paymentId(order.getPaymentId())
                .orderItems(orderItems)
                .address(order.getAddress())
                .orderedAt(createdAt)
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .errorMessage(order.getErrorMessage())
                .build();
    }
}
