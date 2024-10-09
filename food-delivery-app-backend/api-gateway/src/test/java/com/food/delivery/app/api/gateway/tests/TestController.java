package com.food.delivery.app.api.gateway.tests;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private static int CALLS = 0;

    @GetMapping("/api/test")
    public int testRateLimiter() {
        return ++CALLS;
    }
}
