package com.food.delivery.app.command.features.create_order.v1.endpoints;

import com.food.delivery.app.common.utility.security.SecurityContextUtil;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.features.create_order.v1.command.CreateOrderCommandHandler;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.common.utility.datetime.DateTimeUtil;
import com.food.delivery.app.order.command.features.create_order.v1.mapper.OrderCommandMapper;
import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        return ResponseEntity.ok(orderCommandMapper.orderToResponse(order));
    }
}
