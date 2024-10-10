package com.food.delivery.app.order.command.domain;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.shared.exceptions.OrderException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public Order validateOrder(Order order) {
        order.setOrderId(UUID.randomUUID());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
//        order.validateOrder();
        for(OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrderItemId(UUID.randomUUID());
        }
        return order;
    }
}
