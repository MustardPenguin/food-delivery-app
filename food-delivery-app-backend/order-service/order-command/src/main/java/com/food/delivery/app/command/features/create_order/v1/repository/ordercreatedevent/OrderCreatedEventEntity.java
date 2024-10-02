package com.food.delivery.app.command.features.create_order.v1.repository.ordercreatedevent;

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
@Table(name = "order_created_event")
public class OrderCreatedEventEntity {

    @Id
    private UUID eventId;
    private String payload;
    private LocalDateTime createdAt;
}
