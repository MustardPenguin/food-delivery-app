package com.food.delivery.app.restaurant.service.features.get_restaurant_products.v1.grpc;

import com.food.delivery.app.common.proto.ProductServiceGrpc;
import com.food.delivery.app.common.proto.ProductServiceProto;
import com.food.delivery.app.restaurant.service.features.get_restaurant_products.v1.helper.GetProductsHelper;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private final GetProductsHelper getProductsHelper;

    public ProductServiceImpl(GetProductsHelper getProductsHelper) {
        this.getProductsHelper = getProductsHelper;
    }

    public void getProductsByRestaurantId(ProductServiceProto.ProductRequest productRequest, StreamObserver<ProductServiceProto.ProductResponse> responseObserver) {
        UUID restaurantId = UUID.fromString(productRequest.getRestaurantId());
        List<ProductServiceProto.ProductResponse.Product> products = getProductsHelper.getProductsByRestaurantId(restaurantId);
        ProductServiceProto.ProductResponse response = ProductServiceProto.ProductResponse.newBuilder()
                .setRestaurantId(restaurantId.toString())
                .addAllProducts(products)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}