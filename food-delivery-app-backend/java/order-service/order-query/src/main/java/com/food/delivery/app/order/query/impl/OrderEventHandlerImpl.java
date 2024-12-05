package com.food.delivery.app.order.query.impl;

import com.food.delivery.app.order.query.dto.OrderCreatedEvent;
import com.food.delivery.app.order.query.dto.OrderUpdatedEvent;
import com.food.delivery.app.order.query.helper.OrderEventHelper;
import com.food.delivery.app.order.query.ports.OrderEventHandler;
import com.food.delivery.app.order.query.repository.OrderEntity;
import com.food.delivery.app.order.query.repository.OrderJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderEventHandlerImpl implements OrderEventHandler {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEventHelper orderEventHelper;

    public OrderEventHandlerImpl(OrderJpaRepository orderJpaRepository,
                                 OrderEventHelper orderEventHelper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderEventHelper = orderEventHelper;
    }

    @Override
    @Transactional
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        OrderEntity order = orderEventHelper.payloadToOrderEntity(event.getPayload());
        orderJpaRepository.save(order);
        log.info("Successfully saved order of id {} from event of id {} at {}", order.getOrderId(), event.getEventId(), event.getCreatedAt());
    }

    @Override
    public void handleOrderUpdatedEvent(OrderUpdatedEvent event) {
        OrderEntity updatedOrder = orderEventHelper.payloadToOrderEntity(event.getPayload());
        Optional<OrderEntity> order = orderJpaRepository.getByOrderId(updatedOrder.getOrderId());
        if(order.isEmpty()) {
            log.info("Order of id {} not found!", updatedOrder.getOrderId());
            return;
        }
        orderJpaRepository.save(updatedOrder);
        log.info("Successfully updated order of id {} from event of id {} at {}", updatedOrder.getOrderId(), event.getEventId(), event.getCreatedAt());
    }
}
