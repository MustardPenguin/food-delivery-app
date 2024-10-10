package com.food.delivery.app.restaurant.service.config;

import com.food.delivery.app.restaurant.service.shared.exceptions.RestaurantException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestaurantExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RestaurantException.class)
    public ResponseEntity<String> handleOrderExceptions(RestaurantException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
