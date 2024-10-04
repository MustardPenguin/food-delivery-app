package com.food.delivery.app.command.features.create_order.v1.command;

import com.food.delivery.app.command.domain.OrderDomainService;
import com.food.delivery.app.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.command.features.create_order.v1.mapper.OrderCommandMapper;
import com.food.delivery.app.command.features.create_order.v1.repository.ordercreatedevent.OrderCreatedEventEntity;
import com.food.delivery.app.command.domain.entity.Order;
import com.food.delivery.app.command.features.create_order.v1.repository.ordercreatedevent.OrderCreatedEventJpaRepository;
import com.food.delivery.app.command.shared.exceptions.OrderException;
import com.food.delivery.app.command.shared.mapper.OrderMapper;
import com.food.delivery.app.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.command.shared.repository.order.OrderJpaRepository;
import com.food.delivery.app.common.utility.datetime.DateTimeUtil;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateOrderCommandHandler {

    private final OrderCreatedEventJpaRepository orderCreatedEventJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderDomainService orderDomainService;
    private final OrderCommandMapper orderCommandMapper;
    private final ObjectMapperUtil objectMapperUtil;
    private final DateTimeUtil dateTimeUtil;
    private final OrderMapper orderMapper;

    public CreateOrderCommandHandler(OrderCreatedEventJpaRepository orderCreatedEventJpaRepository,
                                     OrderJpaRepository orderJpaRepository,
                                     OrderDomainService orderDomainService,
                                     OrderCommandMapper orderCommandMapper,
                                     ObjectMapperUtil objectMapperUtil,
                                     DateTimeUtil dateTimeUtil,
                                     OrderMapper orderMapper) {
        this.orderCreatedEventJpaRepository = orderCreatedEventJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
        this.orderDomainService = orderDomainService;
        this.orderCommandMapper = orderCommandMapper;
        this.objectMapperUtil = objectMapperUtil;
        this.dateTimeUtil = dateTimeUtil;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public Order handleCreateOrderCommand(CreateOrderCommand createOrderCommand) {
        Order order = orderDomainService.validateOrder(orderCommandMapper.commandToOrder(createOrderCommand));

        String payload = objectMapperUtil.convertObjectToJson(order);

        orderCreatedEventJpaRepository.save(createOrderEvent(payload));
        orderJpaRepository.save(orderMapper.orderToEntity(order));

        return order;
    }

    private OrderCreatedEventEntity createOrderEvent(String payload) {
        return OrderCreatedEventEntity.builder()
                .createdAt(dateTimeUtil.getCurrentDateTime())
                .eventId(UUID.randomUUID())
                .payload(payload)
                .build();
    }
}
