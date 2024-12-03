package com.food.delivery.app.order.command.features.process_order_payments.v1.dto;

import com.food.delivery.app.order.command.domain.valueobjects.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Builder
public class PaymentEventPayload {
    private UUID eventId;
    private UUID paymentId;
    private UUID orderId;
    private UUID customerId;
    private UUID walletId;
    private double amount;
    private PaymentStatus paymentStatus;
    private Timestamp createdAt;
    private String errorMessage;
}
