package com.food.delivery.app.common.domain.event.payload;

import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderEventPayload {

    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    private UUID walletId;
    List<OrderItemEventPayload> orderItems;
    private String address;
    private LocalDateTime orderedAt;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
}
