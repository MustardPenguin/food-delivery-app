package com.food.delivery.app.order.query.message;

import com.food.delivery.app.order.query.dto.OrderUpdatedEvent;
import com.food.delivery.app.order.query.ports.OrderEventHandler;
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
public class OrderUpdatedEventKafkaListener {

    private final OrderEventHandler orderEventHandler;

    public OrderUpdatedEventKafkaListener(OrderEventHandler orderEventHandler) {
        this.orderEventHandler = orderEventHandler;
    }

    @KafkaListener(topics = "order_updated.order_command.order_updated_events", groupId = "order-query")
    public void receiveMessages(List<order_updated.order_command.order_updated_events.Envelope> messages) {
        log.info("Received order updated event messages of size {}", messages.size());
        messages.forEach(message -> {
            order_updated.order_command.order_updated_events.Value value = message.getAfter();
            orderEventHandler.handleOrderUpdatedEvent(messageToEvent(value));
        });
    }

    private OrderUpdatedEvent messageToEvent(order_updated.order_command.order_updated_events.Value value) {
        Instant instant = Instant.ofEpochMilli(value.getCreatedAt());
        LocalDateTime createdAt = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"));
        return OrderUpdatedEvent.builder()
                .eventId(UUID.fromString(value.getEventId()))
                .payload(value.getPayload())
                .createdAt(createdAt)
                .build();
    }
}
