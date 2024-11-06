package com.food.delivery.app.api.gateway.tests;

import com.food.delivery.app.api.gateway.BaseTest;
import org.apache.http.client.utils.URIBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import static org.junit.Assert.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiGatewayTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    public void testRedirect() {
        // Without authentication, should redirect to authorization page
        webTestClient.get()
                .uri("/api/test")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @Order(2)
    public void testObtainAccessToken() throws URISyntaxException {
        // Obtain token from keycloak from credentials
        String uri = keycloak.getAuthServerUrl() + "/realms/" + realmName + "/protocol/openid-connect/token";
        WebClient webClient = WebClient.builder().build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("frontend"));
        formData.put("username", Collections.singletonList("test"));
        formData.put("password", Collections.singletonList("test"));

        String result = webClient.post()
                .uri(new URIBuilder(uri).build())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        JacksonJsonParser jacksonJsonParser = new JacksonJsonParser();
        accessToken = jacksonJsonParser.parseMap(result)
                .get("access_token").toString();
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @Order(3)
    public void testGetProtectedResource() {
        // Access protected resource with token
        webTestClient.get()
                .uri("/api/test")
                .header("Authorization", "Bearer " + accessToken)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Integer.class).isEqualTo(1);
    }
}
