package com.food.delivery.app.order.command.configs;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.food.delivery.app.order.command")
public class FeignConfig {
}
