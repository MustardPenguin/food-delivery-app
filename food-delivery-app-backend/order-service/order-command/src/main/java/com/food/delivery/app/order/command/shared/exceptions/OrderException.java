package com.food.delivery.app.order.command.shared.exceptions;

public class OrderException extends RuntimeException {

    public OrderException(String message) {
        super(message);
    }
}
