package com.food.delivery.app.order.query.impl;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.order.query.dto.OrderCreatedEvent;
import com.food.delivery.app.order.query.helper.OrderEventHelper;
import com.food.delivery.app.order.query.ports.OrderCreatedEventHandler;
import com.food.delivery.app.order.query.repository.OrderEntity;
import com.food.delivery.app.order.query.repository.OrderJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class OrderCreatedEventHandlerImpl implements OrderCreatedEventHandler {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEventHelper orderEventHelper;

    public OrderCreatedEventHandlerImpl(OrderJpaRepository orderJpaRepository,
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
}
