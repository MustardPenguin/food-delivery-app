package com.food.delivery.app.order.command.shared.util;

import com.food.delivery.app.common.proto.ProductServiceGrpc;
import com.food.delivery.app.common.proto.ProductServiceProto;
import com.food.delivery.app.order.command.domain.valueobjects.Product;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class GetProductsUtil {

    @GrpcClient("restaurant-service")
    private ProductServiceGrpc.ProductServiceBlockingStub productServiceStub;

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
}
