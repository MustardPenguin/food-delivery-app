package com.food.delivery.app.order.command.features.process_order_payments.v1.service;

import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.valueobjects.PaymentStatus;
import com.food.delivery.app.order.command.features.process_order_payments.v1.dto.PaymentEventPayload;
import com.food.delivery.app.order.command.shared.mapper.OrderMapper;
import com.food.delivery.app.order.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.order.command.shared.repository.order.OrderJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class OrderPaymentProcessService {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderMapper orderMapper;

    public OrderPaymentProcessService(OrderJpaRepository orderJpaRepository,
                                      OrderMapper orderMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public void processOrderPayments(PaymentEventPayload payment) {
        Optional<OrderEntity> entity = orderJpaRepository.findByOrderId(payment.getOrderId());
        if(entity.isEmpty()) {
            log.info("Error processing payment of id {}, cannot find order of id {}!", payment.getPaymentId(), payment.getOrderId());
            return;
        }
        Order order = orderMapper.orderFromEntity(entity.get());
        log.info("Order to process: {}", order);
        if(payment.getPaymentStatus() == PaymentStatus.COMPLETED) {
//            order.setOrderStatus(OrderStatus.PAID);
        }

    }
}
