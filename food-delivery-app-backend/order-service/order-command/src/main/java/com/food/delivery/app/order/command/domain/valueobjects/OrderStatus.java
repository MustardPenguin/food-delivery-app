package com.food.delivery.app.order.command.domain.valueobjects;

public enum OrderStatus {
    PENDING_PAYMENT, PAID,
    PENDING_APPROVAL, APPROVED,
    CANCELLING, CANCELLED,
}
