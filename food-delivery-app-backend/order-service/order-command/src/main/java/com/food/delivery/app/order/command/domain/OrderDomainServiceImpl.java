package com.food.delivery.app.order.command.domain;

import com.food.delivery.app.order.command.domain.entity.Order;
import com.food.delivery.app.order.command.domain.entity.OrderItem;
import com.food.delivery.app.order.command.domain.valueobjects.OrderStatus;
import com.food.delivery.app.order.command.domain.valueobjects.Product;
import com.food.delivery.app.order.command.shared.exceptions.OrderException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public Order validateOrder(Order order, List<Product> products) {
        order.setOrderId(UUID.randomUUID());
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        BigDecimal total = validateOrderItemsAndGetTotal(order.getOrderItems(), products);
        order.setTotalPrice(total);
        order.validateOrder();

        for(OrderItem orderItem : order.getOrderItems()) {
            orderItem.setOrderItemId(UUID.randomUUID());
        }
        return order;
    }

     private BigDecimal validateOrderItemsAndGetTotal(List<OrderItem> items, List<Product> products) {
        BigDecimal total = new BigDecimal(0);
        Map<UUID, Product> map = new HashMap<>();
        for(Product product : products) {
            map.put(product.getProductId(), product);
        }
        for(OrderItem item : items) {
            Product product = map.getOrDefault(item.getProductId(), null);
            if(product == null) {
                throw new OrderException("Product of id " + item.getProductId() + " not sold by restaurant!");
            }
            if(!product.isAvailable()) {
                throw new OrderException("Product of id " + item.getProductId() + " is not available!");
            }
            if(product.getPrice().compareTo(item.getPrice()) != 0) {
                throw new OrderException("Order item of price " + item.getProductId() + " differs from product price " + product.getPrice()
                        + " for product of " + product.getProductId() + "!");
            }
            BigDecimal productCost = product.getPrice().multiply(new BigDecimal(item.getQuantity()));
            item.setPrice(productCost);
            total = total.add(productCost);
        }
        return total;
     }
}
