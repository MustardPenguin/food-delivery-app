package com.food.delivery.app.restaurant.service.create_product.v1;

import com.food.delivery.app.common.annotation.role.verification.CheckRolesAspect;
import com.food.delivery.app.restaurant.service.domain.entity.Product;
import com.food.delivery.app.common.utility.security.SecurityContextUtil;
import com.food.delivery.app.restaurant.service.BaseTest;
import com.food.delivery.app.restaurant.service.features.create_product.v1.dto.CreateProductCommand;
import com.food.delivery.app.restaurant.service.shared.repository.product.ProductEntity;
import com.food.delivery.app.restaurant.service.shared.repository.product.ProductJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateProductTest extends BaseTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @MockBean
    private CheckRolesAspect checkRolesAspect;
    @MockBean
    private SecurityContextUtil securityContextUtil;

    @LocalServerPort
    private int port;
    private final String url = "http://localhost:" + port;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    final UUID ownerId = UUID.fromString("6b6379b1-b589-4a81-b0d1-2eb1dc3d55f7");
    final UUID restaurantId = UUID.fromString("f86b0456-0a1a-4c90-8f48-136541fb048e");

    @Test
    public void testCreateProduct() {
        final String name = "test name";
        final String description = "test description";
        final boolean available = true;
        final BigDecimal price = new BigDecimal("4.99");

        doNothing().when(checkRolesAspect).checkRole(any());
        when(securityContextUtil.getUUIDFromSecurityContext()).thenReturn(ownerId);

        CreateProductCommand command = CreateProductCommand.builder()
                .name(name)
                .description(description)
                .price(price)
                .available(available)
                .build();

        HttpEntity<CreateProductCommand> entity = new HttpEntity<>(command, headers);

        ResponseEntity<Product> response = restTemplate.exchange(
                url + port + "/api/v1/restaurants/" + restaurantId + "/products",
                HttpMethod.POST, entity, Product.class);

        Product body = response.getBody();

        assertEquals(name, body.getName());
        assertEquals(description, body.getDescription());
        assertEquals(available, body.isAvailable());
        assertEquals(price, body.getPrice());
        assertNotNull(body.getProductId());

        Optional<ProductEntity> optionalProductEntity = productJpaRepository.findById(body.getProductId());
        assertEquals(true, optionalProductEntity.isPresent());
        ProductEntity product = optionalProductEntity.get();
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(available, product.isAvailable());
        assertEquals(price, product.getPrice());
    }
}
