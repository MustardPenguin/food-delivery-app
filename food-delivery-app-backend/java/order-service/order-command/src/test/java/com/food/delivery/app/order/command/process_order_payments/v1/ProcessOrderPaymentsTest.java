package com.food.delivery.app.order.command.process_order_payments.v1;

import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.BaseTest;
import com.food.delivery.app.order.command.domain.event.payload.PaymentEventPayload;
import com.food.delivery.app.order.command.domain.valueobjects.PaymentStatus;
import com.food.delivery.app.order.command.features.process_order_payments.v1.service.OrderPaymentProcessService;
import com.food.delivery.app.order.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.order.command.shared.repository.order.OrderItemEntity;
import com.food.delivery.app.order.command.shared.repository.order.OrderJpaRepository;
import com.food.delivery.app.order.command.shared.repository.order_updated_event.OrderUpdatedEventEntity;
import com.food.delivery.app.order.command.shared.repository.order_updated_event.OrderUpdatedEventJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProcessOrderPaymentsTest extends BaseTest {

    @Autowired
    private OrderUpdatedEventJpaRepository orderUpdatedEventJpaRepository;
    @Autowired
    private OrderJpaRepository orderJpaRepository;
    @Autowired
    private OrderPaymentProcessService service;

    @Test
    public void processOrderPayments() {
        List<OrderItemEntity> items = new ArrayList<>();
        items.add(OrderItemEntity.builder()
                        .price(new BigDecimal("9.99"))
                        .orderItemId(UUID.randomUUID())
                        .productId(UUID.randomUUID())
                        .quantity(2)
                .build());

        OrderEntity entity = OrderEntity.builder()
                .restaurantId(UUID.randomUUID())
                .orderedAt(LocalDateTime.now())
                .orderStatus(OrderStatus.PAID)
                .customerId(UUID.randomUUID())
                .paymentId(UUID.randomUUID())
                .walletId(UUID.randomUUID())
                .orderId(UUID.randomUUID())
                .totalPrice(new BigDecimal("9.99"))
                .orderItems(items)
                .errorMessage("")
                .address("address")
                .build();
        entity.getOrderItems().get(0).setOrder(entity);
        OrderEntity order = orderJpaRepository.save(entity);

        PaymentEventPayload payment = PaymentEventPayload.builder()
                .CreatedAt(Timestamp.from(Instant.now()))
                .PaymentStatus(PaymentStatus.completed)
                .CustomerId(UUID.randomUUID())
                .PaymentId(UUID.randomUUID())
                .WalletId(UUID.randomUUID())
                .OrderId(order.getOrderId())
                .EventId(UUID.randomUUID())
                .ErrorMessage("test")
                .Amount(50)
                .build();

        service.processOrderPayments(payment);
        Optional<OrderEntity> optOrder = orderJpaRepository.findByOrderId(order.getOrderId());
        assertEquals(true, optOrder.isPresent());
        OrderEntity got = optOrder.get();
        assertEquals(OrderStatus.PAID, got.getOrderStatus());
        assertEquals(payment.getPaymentId(), got.getPaymentId());
        assertEquals("test", payment.getErrorMessage());

        List<OrderUpdatedEventEntity> updatedEvents = orderUpdatedEventJpaRepository.findAll();
        assertEquals(1, updatedEvents.size());
    }
}
