package com.food.delivery.app.order.command.features.create_order.v1.dtos;

import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.shared.dto.OrderItemRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand {

    @NotNull(message = "Restaurant id must not be null")
    private UUID restaurantId;
    @NotNull(message = "Wallet id must not be null")
    private UUID walletId;
    @NotNull(message = "Order must not be null")
    private List<OrderItemRequest> orderItems;
    @NotBlank(message = "Address must not be null")
    private String address;
}
