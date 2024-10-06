package com.food.delivery.app.order.query.message;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCreatedEventKafkaListener {

    @KafkaListener(topics = "order_created.order_command.order_created_events", groupId = "order-query")
    public void receiveMessages(List<order_created.order_command.order_created_events.Envelope> messages) {
        messages.forEach(message -> {
            order_created.order_command.order_created_events.Value value = message.getAfter();
            System.out.println("Payload: " + value.getPayload());
        });
    }
}
