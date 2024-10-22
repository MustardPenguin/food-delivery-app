package com.food.delivery.app.command.features.create_order.v1.endpoints;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.features.create_order.v1.command.CreateOrderCommandHandler;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.common.utility.datetime.DateTimeUtil;
import com.food.delivery.app.order.command.features.create_order.v1.mapper.OrderCommandMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class CreateOrderEndpoint {

    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final OrderCommandMapper orderCommandMapper;

    public CreateOrderEndpoint(CreateOrderCommandHandler createOrderCommandHandler,
                               OrderCommandMapper orderCommandMapper) {
        this.createOrderCommandHandler = createOrderCommandHandler;
        this.orderCommandMapper = orderCommandMapper;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Validated CreateOrderCommand createOrderCommand) {
        // Fetches account information from oauth2
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UUID customerId = UUID.fromString(authentication.getName());
        log.info("Creating order for customer id {}", customerId);

        Order order = createOrderCommandHandler.handleCreateOrderCommand(createOrderCommand, customerId);
        return ResponseEntity.ok(orderCommandMapper.orderToResponse(order));
    }
}
