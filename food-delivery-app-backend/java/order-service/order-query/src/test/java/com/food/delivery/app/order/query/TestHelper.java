package com.food.delivery.app.order.query;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.event.payload.OrderItemEventPayload;
import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import org.springframework.boot.test.context.TestComponent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestComponent
public class TestHelper {

    protected OrderEventPayload createOrderEventPayload() {
        OrderItemEventPayload itemPayload = OrderItemEventPayload.builder()
                .orderItemId(UUID.randomUUID())
                .productId(UUID.randomUUID())
                .quantity(1)
                .price(new BigDecimal("14.99"))
                .build();
        List<OrderItemEventPayload> items = new ArrayList<>();
        items.add(itemPayload);
        OrderEventPayload orderEventPayload = OrderEventPayload.builder()
                .orderId(UUID.randomUUID())
                .customerId(UUID.randomUUID())
                .restaurantId(UUID.randomUUID())
                .orderStatus(OrderStatus.PENDING_PAYMENT)
                .totalPrice(new BigDecimal("14.99"))
                .orderedAt(LocalDateTime.now())
                .address("address")
                .orderItems(items)
                .build();
        return orderEventPayload;
    }
}
