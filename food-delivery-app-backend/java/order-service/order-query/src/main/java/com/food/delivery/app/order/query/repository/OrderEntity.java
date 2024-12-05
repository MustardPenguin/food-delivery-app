package com.food.delivery.app.order.query.repository;

import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Document(indexName = "orders")
public class OrderEntity {

    @Id
    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    private UUID paymentId;

    @Field(type = FieldType.Nested, includeInParent = true)
    List<OrderItemEntity> orderItems;
    private String address;
    private Long orderedAt;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private String errorMessage;
}
