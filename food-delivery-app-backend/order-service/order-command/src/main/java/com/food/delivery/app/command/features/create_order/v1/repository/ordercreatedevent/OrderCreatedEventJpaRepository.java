package com.food.delivery.app.command.features.create_order.v1.repository.ordercreatedevent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderCreatedEventJpaRepository extends JpaRepository<OrderCreatedEventEntity, UUID> {
}
