package com.food.delivery.app.order.command.domain.entity;

import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.domain.event.payload.PaymentEventPayload;
import com.food.delivery.app.order.command.domain.valueobjects.PaymentStatus;
import com.food.delivery.app.order.command.shared.exceptions.OrderException;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Order {

    private UUID orderId;
    private UUID customerId;
    private UUID restaurantId;
    private UUID walletId;
    private UUID paymentId;

    List<OrderItem> orderItems;
    private String address;
    private LocalDateTime orderedAt;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private String errorMessage;

    public void validateOrder() {
        if(this.getOrderItems().isEmpty()) {
            throw new OrderException("Order has no items! Please add something to your order.");
        }
        if(totalPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new OrderException("Order total price is invalid! Price: " + totalPrice);
        }
    }

    public void validatePayment(PaymentEventPayload payment) {
        switch (payment.getPaymentStatus()) {
            case completed -> {
                this.setPaymentId(payment.getPaymentId());
                this.setOrderStatus(OrderStatus.PAID);
            }
            case failed, canceled, refunded-> {
                this.setErrorMessage(payment.getErrorMessage());
                this.setOrderStatus(OrderStatus.CANCELLED);
            }
        }
    }
}
