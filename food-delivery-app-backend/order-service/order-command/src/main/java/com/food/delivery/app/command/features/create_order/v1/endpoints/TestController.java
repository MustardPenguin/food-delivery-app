package com.food.delivery.app.command.features.create_order.v1.endpoints;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication authentication = ctx.getAuthentication();
        System.out.println("Credits: " + authentication.getCredentials());
        System.out.println("Authorities: " + authentication.getAuthorities());
        System.out.println("Details: " + authentication.getDetails());
        System.out.println("Principal: " + authentication.getPrincipal());
        System.out.println("Name: " + authentication.getName()); // UUID
        return "authenticated!";
    }
}
