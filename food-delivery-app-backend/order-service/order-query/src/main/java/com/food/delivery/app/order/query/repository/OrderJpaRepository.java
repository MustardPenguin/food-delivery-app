package com.food.delivery.app.order.query.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends ElasticsearchRepository<OrderEntity, UUID> {

    Optional<OrderEntity> getByOrderId(UUID orderId);
}
