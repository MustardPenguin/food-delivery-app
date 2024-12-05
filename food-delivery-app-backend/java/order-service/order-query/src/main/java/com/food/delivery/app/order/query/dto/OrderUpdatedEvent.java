package com.food.delivery.app.order.query.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class OrderUpdatedEvent {

    private UUID eventId;
    private String payload;
    private LocalDateTime createdAt;
}
