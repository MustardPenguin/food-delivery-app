package com.food.delivery.app.order.command.features.create_order.v1.endpoint;

import com.food.delivery.app.common.utility.security.SecurityContextUtil;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.features.create_order.v1.command.CreateOrderCommandHandler;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.order.command.features.create_order.v1.mapper.OrderCommandMapper;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
public class CreateOrderEndpoint {

    private final CreateOrderCommandHandler createOrderCommandHandler;
    private final SecurityContextUtil securityContextUtil;
    private final OrderCommandMapper orderCommandMapper;

    public CreateOrderEndpoint(CreateOrderCommandHandler createOrderCommandHandler,
                               SecurityContextUtil securityContextUtil,
                               OrderCommandMapper orderCommandMapper) {
        this.createOrderCommandHandler = createOrderCommandHandler;
        this.securityContextUtil = securityContextUtil;
        this.orderCommandMapper = orderCommandMapper;
    }

    @PostMapping
    @Observed(name = "order-command:create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Validated CreateOrderCommand createOrderCommand) {
        // Fetches account information from oauth2
        UUID customerId = securityContextUtil.getUUIDFromSecurityContext();
        log.info("Creating order for customer id {}", customerId);

        Order order = createOrderCommandHandler.handleCreateOrderCommand(createOrderCommand, customerId);
        log.info("Successfully created order of id {} for customer id {} at {}", order.getOrderId(), customerId, order.getOrderedAt());

        return ResponseEntity.ok(orderCommandMapper.orderToResponse(order));
    }
}
