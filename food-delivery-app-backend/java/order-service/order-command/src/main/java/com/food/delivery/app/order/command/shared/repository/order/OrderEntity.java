package com.food.delivery.app.order.command.shared.repository.order;

import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderEntity {

    @Id
    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    private UUID walletId;
    private UUID paymentId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    List<OrderItemEntity> orderItems;
    private String address;
    private LocalDateTime orderedAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private String errorMessage;
}
