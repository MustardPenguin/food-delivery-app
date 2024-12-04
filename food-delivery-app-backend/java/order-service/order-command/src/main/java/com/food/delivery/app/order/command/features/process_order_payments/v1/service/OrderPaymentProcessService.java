package com.food.delivery.app.order.command.features.process_order_payments.v1.service;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.common.utility.datetime.DateTimeUtil;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.event.payload.PaymentEventPayload;
import com.food.delivery.app.order.command.shared.mapper.OrderMapper;
import com.food.delivery.app.order.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.order.command.shared.repository.order.OrderJpaRepository;
import com.food.delivery.app.order.command.shared.repository.order_updated_event.OrderUpdatedEventEntity;
import com.food.delivery.app.order.command.shared.repository.order_updated_event.OrderUpdatedEventJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderPaymentProcessService {

    private final OrderUpdatedEventJpaRepository eventJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final ObjectMapperUtil objectMapper;
    private final OrderMapper orderMapper;
    private final DateTimeUtil time;

    public OrderPaymentProcessService(OrderUpdatedEventJpaRepository eventJpaRepository,
                                      OrderJpaRepository orderJpaRepository,
                                      ObjectMapperUtil objectMapper,
                                      OrderMapper orderMapper,
                                      DateTimeUtil time) {
        this.eventJpaRepository = eventJpaRepository;
        this.orderJpaRepository = orderJpaRepository;
        this.objectMapper = objectMapper;
        this.orderMapper = orderMapper;
        this.time = time;
    }

    @Transactional
    public void processOrderPayments(PaymentEventPayload payment) {
        Optional<OrderEntity> entity = orderJpaRepository.findByOrderId(payment.getOrderId());
        if(entity.isEmpty()) {
            log.info("Error processing payment of id {}, cannot find order of id {}!", payment.getPaymentId(), payment.getOrderId());
            return;
        }
        Order order = orderMapper.orderFromEntity(entity.get());
        order.validatePayment(payment);

        OrderEntity saved = orderJpaRepository.save(orderMapper.orderToEntity(order));
        OrderEventPayload payload = orderMapper.orderToEventPayload(orderMapper.orderFromEntity(saved));
        String json = objectMapper.convertObjectToJson(payload);
        OrderUpdatedEventEntity event = createEventEntity(json);
        eventJpaRepository.save(event);
    }

    private OrderUpdatedEventEntity createEventEntity(String payload) {
        return OrderUpdatedEventEntity.builder()
                .eventId(UUID.randomUUID())
                .payload(payload)
                .createdAt(time.getCurrentDateTime())
                .build();
    }
}
