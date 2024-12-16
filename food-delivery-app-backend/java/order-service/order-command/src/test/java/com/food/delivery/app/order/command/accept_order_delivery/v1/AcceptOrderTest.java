package com.food.delivery.app.order.command.accept_order_delivery.v1;

import com.food.delivery.app.common.annotation.role.verification.CheckRolesAspect;
import com.food.delivery.app.common.utility.security.SecurityContextUtil;
import com.food.delivery.app.order.command.BaseTest;
import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.features.create_order.v1.dtos.CreateOrderResponse;
import com.food.delivery.app.order.command.shared.repository.order.OrderEntity;
import com.food.delivery.app.order.command.shared.repository.order.OrderJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptOrderTest extends BaseTest {

    @MockBean
    private CheckRolesAspect checkRolesAspect;
    @MockBean
    private SecurityContextUtil securityContextUtil;
    @Autowired
    private OrderJpaRepository orderJpaRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private int port;
    private final String url = "http://localhost:" + port;

    @Test
    public void testAcceptOrder() {
        UUID driverId = UUID.randomUUID();
        when(securityContextUtil.getUUIDFromSecurityContext()).thenReturn(driverId);
        doNothing().when(checkRolesAspect).checkRole(any());

        List<OrderEntity> orders = orderJpaRepository.findAll();
        OrderEntity order = orders.get(0);
        UUID orderId = order.getOrderId();
        String host = url + port + "/api/v1/orders/" + orderId + "/delivery";

        ResponseEntity<Order> response = restTemplate.exchange(
                host, HttpMethod.POST, null, Order.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Order orderResponse = response.getBody();
        assertEquals(orderId, orderResponse.getOrderId());
        assertEquals(driverId, orderResponse.getDriverId());
    }
}
