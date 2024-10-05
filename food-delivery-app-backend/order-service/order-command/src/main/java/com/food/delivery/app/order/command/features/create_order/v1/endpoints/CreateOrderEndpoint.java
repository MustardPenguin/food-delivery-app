package com.food.delivery.app.command.features.create_order.v1.endpoints;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.features.create_order.v1.command.CreateOrderCommandHandler;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.common.utility.datetime.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class CreateOrderEndpoint {

    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final DateTimeUtil dateTimeUtil;

    public CreateOrderEndpoint(CreateOrderCommandHandler createOrderCommandHandler, DateTimeUtil dateTimeUtil) {
        this.createOrderCommandHandler = createOrderCommandHandler;
        this.dateTimeUtil = dateTimeUtil;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Validated CreateOrderCommand createOrderCommand) {
        // Fetches account information from oauth2
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        UUID customerId = UUID.fromString(authentication.getName());
        log.info("Creating order for customer id {}", customerId);

        createOrderCommand.setCustomerId(customerId);
        createOrderCommand.setOrderedAt(dateTimeUtil.getCurrentDateTime());

        Order order = createOrderCommandHandler.handleCreateOrderCommand(createOrderCommand);
        return ResponseEntity.ok(null);
    }
}
