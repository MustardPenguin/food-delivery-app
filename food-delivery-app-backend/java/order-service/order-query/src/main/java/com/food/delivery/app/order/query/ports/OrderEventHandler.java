package com.food.delivery.app.order.query.ports;

import com.food.delivery.app.order.query.dto.OrderCreatedEvent;
import com.food.delivery.app.order.query.dto.OrderUpdatedEvent;

public interface OrderEventHandler {

    void handleOrderCreatedEvent(OrderCreatedEvent event);
    void handleOrderUpdatedEvent(OrderUpdatedEvent event);
}
