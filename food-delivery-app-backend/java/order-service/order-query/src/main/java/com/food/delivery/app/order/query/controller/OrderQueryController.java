package com.food.delivery.app.order.query.controller;

import com.food.delivery.app.order.query.repository.OrderEntity;
import com.food.delivery.app.order.query.repository.OrderJpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderQueryController {

    private final OrderJpaRepository orderJpaRepository;

    public OrderQueryController(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderEntity> getOrderById(@PathVariable UUID orderId) {
        Optional<OrderEntity> order = orderJpaRepository.getByOrderId(orderId);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }
}
