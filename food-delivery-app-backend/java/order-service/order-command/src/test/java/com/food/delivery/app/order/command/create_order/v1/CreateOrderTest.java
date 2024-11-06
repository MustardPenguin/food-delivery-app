package com.food.delivery.app.order.command.create_order.v1;

import com.food.delivery.app.common.domain.event.payload.OrderEventPayload;
import com.food.delivery.app.common.domain.event.payload.OrderItemEventPayload;
import com.food.delivery.app.common.proto.ProductServiceProto;
import com.food.delivery.app.common.utility.objectmapper.ObjectMapperUtil;
import com.food.delivery.app.common.utility.security.SecurityContextUtil;
import com.food.delivery.app.order.command.BaseTest;
import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.domain.valueobjects.Product;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderCommand;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.order.command.features.create_order.v1.repository.ordercreatedevent.OrderCreatedEventEntity;
import com.food.delivery.app.order.command.features.create_order.v1.repository.ordercreatedevent.OrderCreatedEventJpaRepository;
import com.food.delivery.app.order.command.shared.dto.OrderItemRequest;
import com.food.delivery.app.order.command.shared.util.GetProductsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateOrderTest extends BaseTest {

    @MockBean
    private SecurityContextUtil securityContextUtil;
    @MockBean
    private GetProductsUtil getProductsUtil;
    @Autowired
    private OrderCreatedEventJpaRepository orderCreatedEventJpaRepository;
    @Autowired
    private ObjectMapperUtil objectMapperUtil;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private int port;
    private final String url = "http://localhost:" + port;

    @Test
    public void testCreateOrder() {

        final UUID customerId = UUID.randomUUID();
        final UUID restaurantId = UUID.randomUUID();
        final UUID productId = UUID.randomUUID();
        final BigDecimal price = new BigDecimal("9.99");

        var t = Product.builder().build();

        when(securityContextUtil.getUUIDFromSecurityContext()).thenReturn(customerId);
        when(getProductsUtil.getProductsByRestaurantId(restaurantId)).thenReturn(List.of(
                Product.builder()
                        .productId(productId)
                        .available(true)
                        .price(price)
                        .build()));

        List<OrderItemRequest> orderItems = new ArrayList<>();
        orderItems.add(OrderItemRequest.builder()
                        .quantity(1)
                        .productId(productId)
                        .price(price).build());

        CreateOrderCommand command = CreateOrderCommand.builder()
                .restaurantId(restaurantId)
                .orderItems(orderItems)
                .address("address")
                .build();

        HttpEntity<CreateOrderCommand> entity = new HttpEntity<>(command, headers);
        ResponseEntity<CreateOrderResponse> response = restTemplate.exchange(
                url + port + "/api/v1/orders", HttpMethod.POST, entity, CreateOrderResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        CreateOrderResponse body = response.getBody();
        assertEquals(customerId, body.getCustomerId());
        assertEquals(restaurantId, body.getRestaurantId());
        assertEquals(0, price.compareTo(body.getTotalPrice()));
        OrderItem item = body.getOrderItems().get(0);
        assertNotNull(item);
        assertEquals(productId, item.getProductId());
        assertEquals(0, price.compareTo(item.getPrice()));

        List<OrderCreatedEventEntity> events = orderCreatedEventJpaRepository.findAll();
        assertEquals(1, events.size());
        OrderCreatedEventEntity event = events.get(0);
        OrderEventPayload payload = objectMapperUtil.convertJsonToObject(event.getPayload(), OrderEventPayload.class);
        assertEquals(customerId, payload.getCustomerId());
        assertEquals(restaurantId, payload.getRestaurantId());
        assertEquals(0, price.compareTo(payload.getTotalPrice()));
        OrderItemEventPayload itemPayload = payload.getOrderItems().get(0);
        assertEquals(productId, itemPayload.getProductId());
        assertEquals(0, price.compareTo(itemPayload.getPrice()));
    }
}