package com.food.delivery.app.order.query;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.event.payload.OrderItemEventPayload;
import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.order.query.dto.OrderCreatedEvent;
import com.food.delivery.app.order.query.ports.OrderEventHandler;
import com.food.delivery.app.order.query.repository.OrderEntity;
import com.food.delivery.app.order.query.repository.OrderItemEntity;
import com.food.delivery.app.order.query.repository.OrderJpaRepository;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderCreatedTest extends BaseTest {

    @Autowired
    private OrderEventHandler orderEventHandler;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private ObjectMapperUtil objectMapperUtil;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testOrderCreatedEvent() {

        OrderEventPayload payload = testHelper.createOrderEventPayload();
        String json = objectMapperUtil.convertObjectToJson(payload);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .payload(json)
                .build();

        orderEventHandler.handleOrderCreatedEvent(event);

        Optional<OrderEntity> optionalOrderEntity = orderJpaRepository.getByOrderId(payload.getOrderId());
        assertEquals(true, optionalOrderEntity.isPresent());
        OrderEntity order = optionalOrderEntity.get();


        assertEquals(payload.getOrderId(), order.getOrderId());
        assertEquals(payload.getCustomerId(), order.getCustomerId());
        assertEquals(payload.getRestaurantId(), order.getRestaurantId());
        assertEquals(payload.getAddress(), order.getAddress());
        LocalDateTime receivedLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(order.getOrderedAt()), ZoneOffset.UTC).withNano(0);
        LocalDateTime gotDateTime = payload.getOrderedAt().withNano(0);
        assertEquals(gotDateTime, receivedLocalDateTime);
        assertEquals(payload.getOrderStatus(), order.getOrderStatus());
        assertEquals(payload.getTotalPrice(), order.getTotalPrice());

        OrderItemEntity item = order.getOrderItems().get(0);
        OrderItemEventPayload itemPayload = payload.getOrderItems().get(0);
        assertEquals(itemPayload.getOrderItemId(), item.getOrderItemId());
        assertEquals(itemPayload.getPrice(), item.getPrice());
        assertEquals(item.getQuantity(), item.getQuantity());
        assertEquals(item.getProductId(), item.getProductId());
    }
}
