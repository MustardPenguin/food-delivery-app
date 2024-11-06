package com.food.delivery.app.order.query.ports;

import com.food.delivery.app.order.query.dto.OrderCreatedEvent;

public interface OrderCreatedEventHandler {

    void handleOrderCreatedEvent(OrderCreatedEvent event);
}
