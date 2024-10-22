package com.food.delivery.app.order.command.domain;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.valueobjects.Product;

import java.util.List;

public interface OrderDomainService {

    Order validateOrder(Order order, List<Product> products);
}
