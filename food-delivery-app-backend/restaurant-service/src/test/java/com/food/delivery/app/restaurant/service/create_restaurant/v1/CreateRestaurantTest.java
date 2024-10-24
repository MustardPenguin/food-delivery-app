package com.food.delivery.app.restaurant.service.create_restaurant.v1;

import com.food.delivery.app.common.annotation.role.verification.CheckRolesAspect;
import com.food.delivery.app.common.utility.security.SecurityContextUtil;
import com.food.delivery.app.restaurant.service.BaseTest;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantCommand;
import com.food.delivery.app.restaurant.service.features.create_restaurant.v1.dto.CreateRestaurantResponse;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantEntity;
import com.food.delivery.app.restaurant.service.shared.repository.restaurant.RestaurantJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateRestaurantTest extends BaseTest {

    @Autowired
    private RestaurantJpaRepository restaurantJpaRepository;

    @MockBean
    private CheckRolesAspect checkRolesAspect;
    @MockBean
    private SecurityContextUtil securityContextUtil;

    @LocalServerPort
    private int port;
    private final String url = "http://localhost:" + port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Test
    public void testValidations() {

        doNothing().when(checkRolesAspect).checkRole(any());
        when(securityContextUtil.getUUIDFromSecurityContext()).thenReturn(UUID.randomUUID());

        CreateRestaurantCommand command = CreateRestaurantCommand.builder()
                .restaurantName("")
                .address("")
                .build();
        HttpEntity<CreateRestaurantCommand> entity = new HttpEntity<>(command, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url + port + "/api/v1/restaurants", HttpMethod.POST, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testCreateRestaurant() {

        final UUID ownerId = UUID.randomUUID();
        final String name = "test name";
        final String address = "test address";

        doNothing().when(checkRolesAspect).checkRole(any());
        when(securityContextUtil.getUUIDFromSecurityContext()).thenReturn(ownerId);

        CreateRestaurantCommand command = CreateRestaurantCommand.builder()
                .restaurantName(name)
                .address(address)
                .build();
        HttpEntity<CreateRestaurantCommand> entity = new HttpEntity<>(command, headers);

        ResponseEntity<CreateRestaurantResponse> response = restTemplate.exchange(
                url + port + "/api/v1/restaurants", HttpMethod.POST, entity, CreateRestaurantResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        CreateRestaurantResponse body = response.getBody();
        // Validates that command and response is equal
        assertEquals(name, body.getRestaurantName());
        assertEquals(address, body.getAddress());
        assertEquals(ownerId, body.getOwnerId());

        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantJpaRepository.findByRestaurantId(body.getRestaurantId());
        assertEquals(true, optionalRestaurantEntity.isPresent());
        // Validates that command/response and entity is equal
        RestaurantEntity restaurant = optionalRestaurantEntity.get();
        assertEquals(name, restaurant.getName());
        assertEquals(address, restaurant.getAddress());
        assertEquals(ownerId, restaurant.getOwnerId());
        assertEquals(false, restaurant.isActive());
    }
}
