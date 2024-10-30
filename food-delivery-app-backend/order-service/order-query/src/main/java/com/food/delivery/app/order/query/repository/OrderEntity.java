package com.food.delivery.app.order.query.repository;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.query.helper.CustomZonedDateTimeConverter;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Field(type = FieldType.Nested, includeInParent = true)
    List<OrderItemEntity> orderItems;
    private String address;
    private Long orderedAt;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
}
