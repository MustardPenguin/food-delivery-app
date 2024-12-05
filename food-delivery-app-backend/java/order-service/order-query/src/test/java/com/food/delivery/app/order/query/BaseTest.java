package com.food.delivery.app.order.query;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.event.payload.OrderItemEventPayload;
import com.food.delivery.app.common.domain.valueobjects.OrderStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Import(TestHelper.class)
public class BaseTest {

    @Container
    private static final ElasticsearchContainer elasticSearchContainer = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.7.0"))
            .withEnv("xpack.security.enabled", "false")
            .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", elasticSearchContainer::getHttpHostAddress);
    }

    @BeforeAll
    static void beforeAll() {
        elasticSearchContainer.start();
    }

    @AfterAll
    static void afterAll() {
        elasticSearchContainer.stop();
    }
}
