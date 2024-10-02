package com.food.delivery.app.command.configs;

import com.food.delivery.app.command.shared.exceptions.OrderException;
import jakarta.persistence.criteria.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = OrderException.class)
    public ResponseEntity<String> handleOrderExceptions(OrderException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
