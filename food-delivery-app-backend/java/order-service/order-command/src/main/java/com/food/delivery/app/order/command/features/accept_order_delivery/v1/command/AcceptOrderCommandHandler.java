package com.food.delivery.app.order.command.features.accept_order_delivery.v1.command;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.shared.exceptions.OrderException;
import com.food.delivery.app.order.command.shared.mapper.OrderMapper;
import com.food.delivery.app.order.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.order.command.shared.repository.order.OrderJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AcceptOrderCommandHandler {

    private final OrderJpaRepository repository;
    private final OrderMapper orderMapper;
    private final OrderJpaRepository orderJpaRepository;

    public AcceptOrderCommandHandler(OrderJpaRepository repository,
                                     OrderMapper orderMapper, OrderJpaRepository orderJpaRepository) {
        this.repository = repository;
        this.orderMapper = orderMapper;
        this.orderJpaRepository = orderJpaRepository;
    }

    public Order handleAcceptOrderCommand(UUID orderId, UUID driverId) {
        Optional<OrderEntity> entity = repository.findByOrderId(orderId);
        if(entity.isEmpty()) {
            throw new OrderException("Order of id " + orderId + " does not exist!");
        }
        OrderEntity orderEntity = entity.get();
        Order order = orderMapper.orderFromEntity(orderEntity);
        order.acceptOrderDelivery(driverId);
        orderEntity = orderMapper.orderToEntity(order);
        return orderMapper.orderFromEntity(orderJpaRepository.save(orderEntity));
    }
}
