package com.food.delivery.app.api.gateway;

import com.redis.testcontainers.RedisContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public class BaseTest {

    protected final static String realmName = "food-delivery-app";
    protected static String accessToken;

    @Container
    protected static KeycloakContainer keycloak = new KeycloakContainer()
            .withRealmImportFile("realm-export.json");

    protected static RedisContainer redis = new RedisContainer(RedisContainer.DEFAULT_IMAGE_NAME.withTag(RedisContainer.DEFAULT_TAG));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri", () ->
                keycloak.getAuthServerUrl() + "/realms/" + realmName);
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () ->
                keycloak.getAuthServerUrl() + "/realms/" + realmName + "/protocol/openid-connect/certs");
        registry.add("spring.cloud.gateway.routes[0].uri", () -> "http://localhost:8080");
        registry.add("spring.cloud.gateway.routes[0].predicates[0]", () -> "Path=/api/**");
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }

    @BeforeAll
    static void beforeAll() {
        keycloak.start();
        redis.start();
    }

    @AfterAll
    static void afterAll() {
        keycloak.stop();
        redis.stop();
    }
}
