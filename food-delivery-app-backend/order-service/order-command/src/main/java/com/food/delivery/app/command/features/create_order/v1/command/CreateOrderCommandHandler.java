package com.food.delivery.app.command.features.create_order.v1.command;

import com.food.delivery.app.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.command.features.create_order.v1.mapper.OrderCommandMapper;
import com.food.delivery.app.command.features.create_order.v1.repository.ordercreatedevent.OrderCreatedEventEntity;
import com.food.delivery.app.command.shared.entity.Order;
import com.food.delivery.app.command.shared.exceptions.OrderException;
import com.food.delivery.app.command.shared.repository.order.OrderJpaRepository;
import com.food.delivery.app.common.utility.datetime.DateTimeUtil;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateOrderCommandHandler {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderCommandMapper orderCommandMapper;
    private final ObjectMapperUtil objectMapperUtil;
    private final DateTimeUtil dateTimeUtil;

    public CreateOrderCommandHandler(OrderJpaRepository orderJpaRepository, OrderCommandMapper orderCommandMapper, ObjectMapperUtil objectMapperUtil, DateTimeUtil dateTimeUtil) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderCommandMapper = orderCommandMapper;
        this.objectMapperUtil = objectMapperUtil;
        this.dateTimeUtil = dateTimeUtil;
    }

    @Transactional
    public CreateOrderResponse handleCreateOrderCommand(CreateOrderCommand createOrderCommand) {
        Order order = orderCommandMapper.commandToOrder(createOrderCommand);
        if(order.getOrderItems().isEmpty()) {
            throw new OrderException("Order has no items! Please add something to your order.");
        }
        
        return null;
//        OrderEntity orderEntity = createOrderMapper.createOrderCommandToEntity(createOrderCommand);
//        OrderEntity savedEntity = orderJpaRepository.save(orderEntity);
//
//        String payload = objectMapperUtil.convertObjectToJson(orderEntity);
//        OrderCreatedEventEntity orderCreatedEventEntity = createOrderEvent(payload);
//
//        return createOrderMapper.orderEntityToResponse(savedEntity);
    }

    private OrderCreatedEventEntity createOrderEvent(String payload) {
        return OrderCreatedEventEntity.builder()
                .createdAt(dateTimeUtil.getCurrentDateTime())
                .eventId(UUID.randomUUID())
                .payload(payload)
                .build();
    }
}
