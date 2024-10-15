package com.food.delivery.app.restaurant.service.features.get_restaurant_products.v1.grpc;

import com.food.delivery.app.common.proto.ProductServiceGrpc;
import com.food.delivery.app.common.proto.ProductServiceProto;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    public void getProductsByRestaurantId(ProductServiceProto.ProductRequest productRequest, StreamObserver<ProductServiceProto.ProductResponse> responseObserver) {
        ProductServiceProto.ProductResponse response = ProductServiceProto.ProductResponse.newBuilder()
                .setRestaurantId("test restaurant id")
                .addProducts(ProductServiceProto.ProductResponse.Product.newBuilder()
                        .setProductId("test product id")
                        .setAvailable(true)
                        .setPrice(4.99)
                        .build())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}