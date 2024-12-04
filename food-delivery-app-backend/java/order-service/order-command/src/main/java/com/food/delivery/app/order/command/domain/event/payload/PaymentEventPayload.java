package com.food.delivery.app.order.command.domain.event.payload;

import com.food.delivery.app.order.command.domain.valueobjects.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Builder
public class PaymentEventPayload {
    // Starts with capital letter for converting from JSON
    private UUID EventId;
    private UUID PaymentId;
    private UUID OrderId;
    private UUID CustomerId;
    private UUID WalletId;
    private double Amount;
    private PaymentStatus PaymentStatus;
    private Timestamp CreatedAt;
    private String ErrorMessage;
}
