package com.food.delivery.app.order.command.features.accept_order_delivery.v1.endpoint;

import com.food.delivery.app.common.annotation.role.verification.CheckRoles;
import com.food.delivery.app.common.utility.security.SecurityContextUtil;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.features.accept_order_delivery.v1.command.AcceptOrderCommandHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class AcceptOrderEndpoint {

    private final SecurityContextUtil securityContextUtil;
    private final AcceptOrderCommandHandler commandHandler;

    public AcceptOrderEndpoint(SecurityContextUtil securityContextUtil,
                               AcceptOrderCommandHandler commandHandler) {
        this.securityContextUtil = securityContextUtil;
        this.commandHandler = commandHandler;
    }

    @PostMapping("/orders/{orderId}/delivery")
    @CheckRoles(roles = {"driver"})
    public ResponseEntity<Order> acceptOrder(@PathVariable UUID orderId) {
        UUID driverId = securityContextUtil.getUUIDFromSecurityContext();
        Order order = commandHandler.handleAcceptOrderCommand(orderId, driverId);

        return ResponseEntity.ok(order);
    }
}
