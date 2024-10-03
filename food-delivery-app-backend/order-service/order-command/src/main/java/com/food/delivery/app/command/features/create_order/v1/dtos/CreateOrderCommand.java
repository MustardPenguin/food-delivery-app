package com.food.delivery.app.command.features.create_order.v1.dtos;

import com.food.delivery.app.command.domain.entity.OrderItem;
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
    @NotNull(message = "Order must not be null")
    private List<OrderItem> orderItems;
    @NotBlank(message = "Address must not be null")
    private String address;

    private LocalDateTime orderedAt;
    private UUID customerId;
}
