package com.ecommerce.api.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RouteLocatorConfig {

//    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route("Orders route", route -> route
                        .path("/api/v1/orders/**")
                        .uri("http://localhost:8181/"))
                .route("any", route -> route
                        .path("/**")
                        .uri("http://localhost:8181/"))
                .build();
    }
}
