package com.food.delivery.app.command.domain;

import com.food.delivery.app.command.domain.entity.Order;

public interface OrderDomainService {

    Order validateOrder(Order order);
}
