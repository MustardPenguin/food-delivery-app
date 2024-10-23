package com.food.delivery.app.restaurant.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DirtiesContext
public class BaseTest {

    // Disable security
    @MockBean
    private SecurityFilterChain securityFilterChain;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withUrlParam("stringtype", "unspecified")
            .withUrlParam("currentSchema", "restaurant")
            .withReuse(true);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        // Override initial schema setup
        registry.add("spring.sql.init.schema-locations", () -> "classpath:init-schema-test.sql");
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}
