package com.food.delivery.app.order.command.features.process_order_payments.v1.message;

import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.order.command.features.process_order_payments.v1.dto.PaymentCreatedEvent;
import com.food.delivery.app.order.command.domain.event.payload.PaymentEventPayload;
import com.food.delivery.app.order.command.features.process_order_payments.v1.service.OrderPaymentProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class PaymentCreatedMessageListener {

    private final OrderPaymentProcessService service;
    private final ObjectMapperUtil objectMapper;

    public PaymentCreatedMessageListener(OrderPaymentProcessService service, ObjectMapperUtil objectMapper) {
        this.service = service;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "payment_created.payment.payment_created_events", groupId = "order-command")
    public void receiveMessages(List<payment_created.payment.payment_created_events.Envelope> messages) {
        log.info("Received payment created event messages of size {}", messages.size());
        messages.forEach(message -> {
            payment_created.payment.payment_created_events.Value value = message.getAfter();
            log.info("Processing message value: {}", value);
            PaymentCreatedEvent event = messageToEvent(value);
            PaymentEventPayload payload = objectMapper.convertJsonToObject(event.getPayload(), PaymentEventPayload.class);
            service.processOrderPayments(payload);
        });
    }

    private PaymentCreatedEvent messageToEvent(payment_created.payment.payment_created_events.Value value) {
        Instant instant = Instant.ofEpochMilli(value.getCreatedAt());
        LocalDateTime createdAt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        return PaymentCreatedEvent.builder()
                .eventId(UUID.fromString(value.getEventId()))
                .payload(value.getPayload())
                .createdAt(createdAt)
                .build();
    }
}
