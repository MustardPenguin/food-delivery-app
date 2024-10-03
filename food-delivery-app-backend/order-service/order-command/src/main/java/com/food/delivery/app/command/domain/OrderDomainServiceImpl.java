package com.food.delivery.app.command.domain;

import com.food.delivery.app.command.domain.entity.Order;
import com.food.delivery.app.command.shared.exceptions.OrderException;
import org.springframework.stereotype.Service;

@Service
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public Order validateOrder(Order order) {
        if(order.getOrderItems().isEmpty()) {
            throw new OrderException("Order has no items! Please add something to your order.");
        }
        return order;
    }
}
