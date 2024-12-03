package com.food.delivery.app.order.command.features.process_order_payments.v1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentCreatedEvent {
    private UUID eventId;
    private String payload;
    private LocalDateTime createdAt;
}
