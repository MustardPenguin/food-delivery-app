package com.food.delivery.app.order.query;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.event.payload.OrderItemEventPayload;
import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.order.query.dto.OrderCreatedEvent;
import com.food.delivery.app.order.query.dto.OrderUpdatedEvent;
import com.food.delivery.app.order.query.ports.OrderEventHandler;
import com.food.delivery.app.order.query.repository.OrderEntity;
import com.food.delivery.app.order.query.repository.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderUpdatedTest extends BaseTest {

    @Autowired
    private ObjectMapperUtil objectMapperUtil;
    @Autowired
    private OrderEventHandler orderEventHandler;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private TestHelper testHelper;

    @Test
    public void testOrderUpdatedEvent() {

        OrderEventPayload payload = testHelper.createOrderEventPayload();
        String json = objectMapperUtil.convertObjectToJson(payload);
        OrderCreatedEvent createdEvent = OrderCreatedEvent.builder()
                .eventId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .payload(json)
                .build();
        orderEventHandler.handleOrderCreatedEvent(createdEvent);

        payload.setErrorMessage("new error message");
        payload.setAddress("new address");
        payload.setOrderStatus(OrderStatus.PAID);
        json = objectMapperUtil.convertObjectToJson(payload);
        OrderUpdatedEvent updatedEvent = OrderUpdatedEvent.builder()
                .eventId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .payload(json)
                .build();
        orderEventHandler.handleOrderUpdatedEvent(updatedEvent);
        Optional<OrderEntity> opt = orderJpaRepository.getByOrderId(payload.getOrderId());
        assertEquals(true, opt.isPresent());
        OrderEntity order = opt.get();
        assertEquals(order.getOrderId(), order.getOrderId());
        assertEquals(payload.getErrorMessage(), order.getErrorMessage());
        assertEquals(payload.getOrderStatus(), order.getOrderStatus());
        assertEquals(payload.getAddress(), order.getAddress());
    }
}
