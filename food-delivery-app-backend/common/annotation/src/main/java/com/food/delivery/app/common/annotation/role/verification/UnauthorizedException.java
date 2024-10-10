package com.food.delivery.app.common.annotation.role.verification;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
