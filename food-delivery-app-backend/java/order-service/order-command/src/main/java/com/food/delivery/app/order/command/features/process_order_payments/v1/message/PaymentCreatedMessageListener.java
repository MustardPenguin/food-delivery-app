package com.food.delivery.app.order.command.features.process_order_payments.v1.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PaymentCreatedMessageListener {
    
    @KafkaListener(topics = "payment_created.payment.payment_created_events", groupId = "order-command")
    public void receiveMessages(List<payment_created.payment.payment_created_events.Envelope> messages) {
        log.info("Received payment created event messages of size {}", messages.size());
        messages.forEach(message -> {
            payment_created.payment.payment_created_events.Value value = message.getAfter();
            log.info("Processing message of event id {} created at {}", value.getEventId(), value.getCreatedAt());
            log.info("Value: {}", value);
        });
    }
}
