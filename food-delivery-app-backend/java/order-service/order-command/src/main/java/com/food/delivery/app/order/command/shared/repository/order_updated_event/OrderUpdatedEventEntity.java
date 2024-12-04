package com.food.delivery.app.order.command.shared.repository.order_updated_event;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_updated_events")
public class OrderUpdatedEventEntity {

    @Id
    private UUID eventId;
    private String payload;
    private LocalDateTime createdAt;
}
