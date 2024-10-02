package com.food.delivery.app.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.food.delivery.app"})
public class OrderCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderCommandApplication.class, args);
    }
}
