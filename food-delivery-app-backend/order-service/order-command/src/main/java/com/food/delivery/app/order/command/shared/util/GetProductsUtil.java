package com.food.delivery.app.order.command.shared.util;

import com.food.delivery.app.common.proto.ProductServiceGrpc;
import com.food.delivery.app.common.proto.ProductServiceProto;
import com.food.delivery.app.order.command.domain.valueobjects.Product;
import com.food.delivery.app.order.command.shared.exceptions.OrderException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class GetProductsUtil {

    @GrpcClient("restaurant-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

    @CircuitBreaker(name = "grpc-circuit-breaker", fallbackMethod = "handleFallback")
    public List<Product> getProductsByRestaurantId(UUID restaurantId) {

        ProductServiceProto.ProductRequest request = ProductServiceProto.ProductRequest.newBuilder()
                .setRestaurantId(restaurantId.toString())
                .build();

        ProductServiceProto.ProductResponse response = productServiceStub.getProductsByRestaurantId(request);

        return response.getProductsList().stream().map(this::productProtoToDomain).toList();
    }

    private Product productProtoToDomain(ProductServiceProto.ProductResponse.Product product) {
        return Product.builder()
                .productId(UUID.fromString(product.getProductId()))
                .price(BigDecimal.valueOf(product.getPrice()))
                .available(product.getAvailable())
                .build();
    }

    private List<Product> handleFallback(Throwable throwable) throws Throwable {
        log.error("Error while calling restaurant grpc service: {}", throwable.getMessage());
        throw new OrderException("Error while calling restaurant grpc service: " + throwable.getMessage());
    }
}
