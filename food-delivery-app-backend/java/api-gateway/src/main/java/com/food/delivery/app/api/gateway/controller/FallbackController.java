package com.food.delivery.app.api.gateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping ("/fallback/{service}")
    public ResponseEntity<Map<String, String>> fallback(@PathVariable String service) {
        Map<String, String> errorMapping = new HashMap<>();
        String errorMessage = service + " service is not available.";
        errorMapping.put("Status", errorMessage);
        return ResponseEntity.internalServerError().body(errorMapping);
    }
}
