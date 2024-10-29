package com.food.delivery.app.order.query.message;

import com.food.delivery.app.order.query.dto.OrderCreatedEvent;
import com.food.delivery.app.order.query.ports.OrderCreatedEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreatedEventKafkaListener {

    private final OrderCreatedEventHandler orderCreatedEventHandler;

    public OrderCreatedEventKafkaListener(OrderCreatedEventHandler orderCreatedEventHandler) {
        this.orderCreatedEventHandler = orderCreatedEventHandler;
    }

    @KafkaListener(topics = "order_created.order_command.order_created_events", groupId = "order-query")
    public void receiveMessages(List<order_created.order_command.order_created_events.Envelope> messages) {
        log.info("Received order created event messages of size {}", messages.size());
        messages.forEach(message -> {
            order_created.order_command.order_created_events.Value value = message.getAfter();
            log.info("Processing message of event id {} created at {}", value.getEventId(), value.getCreatedAt());
            orderCreatedEventHandler.handleOrderCreatedEvent(messageToEvent(value));
        });
    }

    private OrderCreatedEvent messageToEvent(order_created.order_command.order_created_events.Value event) {
        LocalDateTime createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getCreatedAt()),
                TimeZone.getDefault().toZoneId());
        return OrderCreatedEvent.builder()
                .eventId(UUID.fromString(event.getEventId()))
                .payload(event.getPayload())
                .createdAt(createdAt)
                .build();
    }
}
