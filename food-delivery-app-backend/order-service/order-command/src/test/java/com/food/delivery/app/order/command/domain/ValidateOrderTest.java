package com.food.delivery.app.order.command.domain;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.domain.valueobjects.Product;
import com.food.delivery.app.order.command.shared.exceptions.OrderException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@TestComponent
public class ValidateOrderTest {

    private static final List<Product> products = new ArrayList<>();
    private final OrderDomainServiceImpl orderDomainService = new OrderDomainServiceImpl();

    @BeforeAll
    static void init() {
        products.add(Product.builder()
                .productId(UUID.fromString("e8b31d73-b44b-461a-8fe5-072913232a0d"))
                .price(new BigDecimal("9.99"))
                .available(true).build());
        products.add(Product.builder()
                .productId(UUID.fromString("e8b31d73-b44b-461a-8fe5-072913232a0c"))
                .price(new BigDecimal("19.99"))
                .available(true).build());
        products.add(Product.builder()
                .productId(UUID.fromString("e8b31d73-b44b-461a-8fe5-072913232a0e"))
                .price(new BigDecimal("15.50"))
                .available(false).build());
    }

    @Test
    public void testValidateOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        UUID customerId = UUID.randomUUID();
        orderItems.add(productToOrderItem(products.get(0)));
        orderItems.add(productToOrderItem(products.get(1)));
        orderItems.get(1).setQuantity(3);

        Order order = Order.builder()
                .customerId(customerId)
                .orderItems(orderItems)
                .build();

        // Calculates total price from order items, then compares it to order entity returned from domain
        BigDecimal total = new BigDecimal("0");
        for(OrderItem item : orderItems) {
            BigDecimal p = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(p);
        }

        Order validatedOrder = orderDomainService.validateOrder(order, products);
        assertEquals(OrderStatus.PENDING_PAYMENT, validatedOrder.getOrderStatus());
        assertEquals(0, total.compareTo(validatedOrder.getTotalPrice()));
        assertEquals(customerId, validatedOrder.getCustomerId());
    }

    @Test
    public void testWrongProduct() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder()
                .productId(UUID.randomUUID())
                .price(new BigDecimal("9.99"))
                .quantity(1)
                .build());

        String message = "Product of id " + orderItems.get(0).getProductId() + " not sold by restaurant!";
        assertOrderException(orderItems, message);
    }

    @Test
    public void testWrongPrice() {
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = products.get(0);
        OrderItem item = productToOrderItem(product);
        item.setPrice(item.getPrice().add(new BigDecimal("1")));
        orderItems.add(item);

        String message = "Order item of price " + item.getProductId() + " differs from product price " + product.getPrice()
                + " for product of " + product.getProductId() + "!";
        assertOrderException(orderItems, message);
    }

    @Test
    public void testUnavailableProduct() {
        List<OrderItem> orderItems = new ArrayList<>();
        Product product = products.get(2);
        OrderItem item = productToOrderItem(product);
        orderItems.add(item);
        String message = "Product of id " + item.getProductId() + " is not available!";
        assertOrderException(orderItems, message);
    }

    private void assertOrderException(List<OrderItem> orderItems, String message) {
        Order order = Order.builder()
                .orderItems(orderItems)
                .build();

        OrderException e = assertThrows(OrderException.class, () -> {
            orderDomainService.validateOrder(order, products);
        });

        assertEquals(message, e.getMessage());
    }

    private OrderItem productToOrderItem(Product product) {
        return OrderItem.builder()
                .productId(product.getProductId())
                .price(product.getPrice())
                .quantity(1)
                .build();
    }
}
