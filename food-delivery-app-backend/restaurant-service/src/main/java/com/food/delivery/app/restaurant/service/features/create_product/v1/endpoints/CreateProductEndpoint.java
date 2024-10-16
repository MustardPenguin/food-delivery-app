package com.food.delivery.app.restaurant.service.features.create_product.v1.endpoints;

import com.food.delivery.app.common.annotation.role.verification.CheckRoles;
import com.food.delivery.app.common.domain.entity.Product;
import com.food.delivery.app.restaurant.service.features.create_product.v1.command.CreateProductCommandHandler;
import com.food.delivery.app.restaurant.service.features.create_product.v1.dto.CreateProductCommand;
import com.food.delivery.app.restaurant.service.shared.util.SecurityContextUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CreateProductEndpoint {

    private final CreateProductCommandHandler commandHandler;
    private final SecurityContextUtil securityContextUtil;

    public CreateProductEndpoint(CreateProductCommandHandler commandHandler,
                                 SecurityContextUtil securityContextUtil) {
        this.commandHandler = commandHandler;
        this.securityContextUtil = securityContextUtil;
    }

    @PostMapping("/restaurants/{restaurantId}/products")
    @CheckRoles(roles = {"restaurant-manager"})
    public ResponseEntity<Product> createProduct(@RequestBody @Validated CreateProductCommand command, @PathVariable UUID restaurantId) {
        UUID accountId = securityContextUtil.getUUIDFromSecurityContext();
        command.setManagerId(accountId);

        Product product = commandHandler.handleCreateProductCommand(command, restaurantId);
        return ResponseEntity.ok(product);
    }
}
