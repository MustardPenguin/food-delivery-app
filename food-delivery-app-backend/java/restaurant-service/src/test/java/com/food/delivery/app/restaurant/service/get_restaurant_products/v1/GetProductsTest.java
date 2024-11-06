package com.food.delivery.app.restaurant.service.get_restaurant_products.v1;

import com.food.delivery.app.common.proto.ProductServiceGrpc;
import com.food.delivery.app.common.proto.ProductServiceProto;
import com.food.delivery.app.restaurant.service.BaseTest;
import com.food.delivery.app.restaurant.service.features.get_restaurant_products.v1.grpc.ProductServiceImpl;
import io.grpc.internal.testing.StreamRecorder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@SpringBootTest
public class GetProductsTest extends BaseTest {

    @Autowired
    private ProductServiceImpl productService;

    // Values from init-schema.sql
    final UUID restaurantId = UUID.fromString("f86b0456-0a1a-4c90-8f48-136541fb048e");
    final UUID productId = UUID.fromString("84b17918-4aa6-4be7-8fde-f61b7e022438");
    final double price = 9.99;
    final boolean available = true;

    @Test
    public void testGetProducts() throws Exception {
        ProductServiceProto.ProductRequest request = ProductServiceProto.ProductRequest.newBuilder()
                .setRestaurantId(restaurantId.toString())
                .build();

        StreamRecorder<ProductServiceProto.ProductResponse> responseObserver = StreamRecorder.create();
        productService.getProductsByRestaurantId(request, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNull(responseObserver.getError());
        assertEquals(1, responseObserver.getValues().size());

        ProductServiceProto.ProductResponse response = responseObserver.getValues().get(0);

        assertEquals(restaurantId.toString(), response.getRestaurantId());
        assertEquals(1, response.getProductsList().size());

        ProductServiceProto.ProductResponse.Product product = response.getProducts(0);
        assertEquals(productId.toString(), product.getProductId());
        assertEquals(available, product.getAvailable());
        assertEquals(price, product.getPrice(), 0);
    }
}