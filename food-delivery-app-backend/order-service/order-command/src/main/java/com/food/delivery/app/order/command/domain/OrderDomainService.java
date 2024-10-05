package com.food.delivery.app.order.command.domain;

import com.food.delivery.app.order.command.domain.entity.Order;

public interface OrderDomainService {

    Order validateOrder(Order order);
}
