package com.food.delivery.app.command.features.create_order.v1.mapper;

import com.food.delivery.app.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.command.shared.entity.Order;
import com.food.delivery.app.command.shared.entity.OrderItem;
import com.food.delivery.app.command.shared.mapper.OrderMapper;
import com.food.delivery.app.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.command.shared.repository.order.OrderItemEntity;
import com.food.delivery.app.command.shared.valueobjects.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderCommandMapper {

    private final OrderMapper orderMapper;

    public OrderCommandMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public Order commandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .restaurantId(createOrderCommand.getRestaurantId())
                .customerId(createOrderCommand.getCustomerId())
                .orderItems(createOrderCommand.getOrderItems())
                .orderedAt(createOrderCommand.getOrderedAt())
                .address(createOrderCommand.getAddress())
                .orderStatus(OrderStatus.PENDING_PAYMENT)
                .orderId(UUID.randomUUID())
                .build();
    }

    public OrderEntity orderToEntity(Order order) {
        return OrderEntity.builder()

                .build();
    }

    public OrderEntity createOrderCommandToEntity(CreateOrderCommand createOrderCommand) {

        OrderEntity orderEntity = OrderEntity.builder()
                .restaurantId(createOrderCommand.getRestaurantId())
                .customerId(createOrderCommand.getCustomerId())
                .orderedAt(createOrderCommand.getOrderedAt())
                .address(createOrderCommand.getAddress())
                .orderId(UUID.randomUUID())
                .build();

        List<OrderItemEntity> orderItemEntities = createOrderCommand.getOrderItems().stream()
                .map(orderItem -> {
                    OrderItemEntity orderItemEntity = orderMapper.orderItemToEntity(orderItem);
                    orderItemEntity.setOrderItemId(UUID.randomUUID());
                    orderItemEntity.setOrder(orderEntity);
                    return orderItemEntity;
                }).toList();

        orderEntity.setOrderItems(orderItemEntities);
        return orderEntity;
    }

    public CreateOrderResponse orderEntityToResponse(OrderEntity orderEntity) {
        List<OrderItem> orderItems = orderEntity.getOrderItems().stream()
                .map(orderMapper::orderItemFromEntity).toList();
        return CreateOrderResponse.builder()
                .restaurantId(orderEntity.getRestaurantId())
                .customerId(orderEntity.getCustomerId())
                .orderedAt(orderEntity.getOrderedAt())
                .orderId(orderEntity.getOrderId())
                .address(orderEntity.getAddress())
                .orderItems(orderItems)
                .build();
    }
}
