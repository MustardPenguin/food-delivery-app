package com.food.delivery.app.order.command.features.create_order.v1.command;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.proto.ProductServiceProto;
import com.food.delivery.app.order.command.domain.OrderDomainService;
import com.food.delivery.app.order.command.domain.valueobjects.Product;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.features.create_order.v1.mapper.OrderCommandMapper;
import com.food.delivery.app.order.command.features.create_order.v1.repository.ordercreatedevent.OrderCreatedEventEntity;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.features.create_order.v1.repository.ordercreatedevent.OrderCreatedEventJpaRepository;
import com.food.delivery.app.order.command.shared.mapper.OrderMapper;
import com.food.delivery.app.order.command.shared.repository.order.OrderJpaRepository;
import com.food.delivery.app.common.utility.datetime.DateTimeUtil;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.order.command.shared.util.GetProductsUtil;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class CreateOrderCommandHandler {

    private final OrderCreatedEventJpaRepository orderCreatedEventJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final OrderDomainService orderDomainService;
    private final OrderCommandMapper orderCommandMapper;
    private final ObjectMapperUtil objectMapperUtil;
    private final GetProductsUtil getProductsUtil;
    private final DateTimeUtil dateTimeUtil;
    private final OrderMapper orderMapper;

    public CreateOrderCommandHandler(OrderCreatedEventJpaRepository orderCreatedEventJpaRepository,
                                     OrderJpaRepository orderJpaRepository,
                                     OrderDomainService orderDomainService,
                                     OrderCommandMapper orderCommandMapper,
                                     ObjectMapperUtil objectMapperUtil,
                                     GetProductsUtil getProductsUtil,
                                     DateTimeUtil dateTimeUtil,
                                     OrderMapper orderMapper) {
        this.orderCreatedEventJpaRepository = orderCreatedEventJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
        this.orderDomainService = orderDomainService;
        this.orderCommandMapper = orderCommandMapper;
        this.objectMapperUtil = objectMapperUtil;
        this.getProductsUtil = getProductsUtil;
        this.dateTimeUtil = dateTimeUtil;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public Order handleCreateOrderCommand(CreateOrderCommand createOrderCommand, UUID customerId) {

        List<Product> products = getProductsUtil.getProductsByRestaurantId(createOrderCommand.getRestaurantId());
        Order order = orderDomainService.validateOrder(orderCommandMapper.commandToOrder(createOrderCommand, customerId), products);
        order.setOrderedAt(dateTimeUtil.getCurrentDateTime());

        OrderEventPayload orderEventPayload = orderMapper.orderToEventPayload(order);
        String payload = objectMapperUtil.convertObjectToJson(orderEventPayload);
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
