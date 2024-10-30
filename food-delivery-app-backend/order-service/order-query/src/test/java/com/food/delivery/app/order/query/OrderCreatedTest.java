package com.food.delivery.app.order.query;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.event.payload.OrderItemEventPayload;
import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.order.query.dto.OrderCreatedEvent;
import com.food.delivery.app.order.query.ports.OrderCreatedEventHandler;
import com.food.delivery.app.order.query.repository.OrderEntity;
import com.food.delivery.app.order.query.repository.OrderItemEntity;
import com.food.delivery.app.order.query.repository.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class OrderCreatedTest extends BaseTest {

    @Autowired
    private OrderCreatedEventHandler orderCreatedEventHandler;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private ObjectMapperUtil objectMapperUtil;

    final UUID expectedOrderId = UUID.randomUUID();
    final UUID expectedCustomerId = UUID.randomUUID();
    final UUID expectedRestaurantId = UUID.randomUUID();
    final OrderStatus expectedOrderStatus = OrderStatus.PENDING_PAYMENT;
    final String expectedAddress = "address";
    // Remove nanoseconds for easier comparison
    final LocalDateTime expectedCreatedAt = LocalDateTime.now().withNano(0);
    final BigDecimal expectedTotalPrice = new BigDecimal("9.99");

    final UUID expectedOrderItemId = UUID.randomUUID();
    final UUID expectedProductId = UUID.randomUUID();
    final int expectedQuantity = 1;
    final BigDecimal expectedItemPrice = new BigDecimal("9.99");

    @Test
    public void testOrderCreatedEvent() {

        OrderEventPayload orderEventPayload = createOrderEventPayload();
        String payload = objectMapperUtil.convertObjectToJson(orderEventPayload);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .payload(payload)
                .build();

        orderCreatedEventHandler.handleOrderCreatedEvent(event);

        Optional<OrderEntity> optionalOrderEntity = orderJpaRepository.getByOrderId(expectedOrderId);
        assertEquals(true, optionalOrderEntity.isPresent());
        OrderEntity order = optionalOrderEntity.get();
        assertEquals(expectedOrderId, order.getOrderId());
        assertEquals(expectedCustomerId, order.getCustomerId());
        assertEquals(expectedRestaurantId, order.getRestaurantId());
        assertEquals(expectedAddress, order.getAddress());
        LocalDateTime receivedLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(order.getOrderedAt()), ZoneOffset.UTC).withNano(0);
        assertEquals(expectedCreatedAt, receivedLocalDateTime);
        assertEquals(expectedOrderStatus, order.getOrderStatus());
        assertEquals(expectedTotalPrice, order.getTotalPrice());

        OrderItemEntity item = order.getOrderItems().get(0);
        assertEquals(expectedOrderItemId, item.getOrderItemId());
        assertEquals(expectedItemPrice, item.getPrice());
        assertEquals(expectedQuantity, item.getQuantity());
        assertEquals(expectedProductId, item.getProductId());
    }

    private OrderEventPayload createOrderEventPayload() {
        OrderItemEventPayload itemPayload = OrderItemEventPayload.builder()
                .orderItemId(expectedOrderItemId)
                .productId(expectedProductId)
                .quantity(expectedQuantity)
                .price(expectedItemPrice)
                .build();
        List<OrderItemEventPayload> items = new ArrayList<>();
        items.add(itemPayload);
        OrderEventPayload orderEventPayload = OrderEventPayload.builder()
                .orderId(expectedOrderId)
                .customerId(expectedCustomerId)
                .restaurantId(expectedRestaurantId)
                .orderStatus(expectedOrderStatus)
                .totalPrice(expectedTotalPrice)
                .orderedAt(expectedCreatedAt)
                .address(expectedAddress)
                .orderItems(items)
                .build();
        return orderEventPayload;
    }
}
