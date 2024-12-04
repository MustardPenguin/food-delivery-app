package com.food.delivery.app.order.command.shared.repository.order_updated_event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderUpdatedEventJpaRepository extends JpaRepository<OrderUpdatedEventEntity, UUID> {
}
