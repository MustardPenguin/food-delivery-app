package com.food.delivery.app.order.command.domain;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.shared.exceptions.OrderException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public Order validateOrder(Order order) {
        order.setOrderId(UUID.randomUUID());
        if(order.getOrderItems().isEmpty()) {
            throw new OrderException("Order has no items! Please add something to your order.");
        }
        for(OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrderItemId(UUID.randomUUID());
        }
        return order;
    }
}
