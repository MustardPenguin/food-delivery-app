package com.food.delivery.app.common.annotation.role.verification;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class CheckRolesAspect {

    @Before("@annotation(checkRoles)")
    public void checkRole(CheckRoles checkRoles) {
        String[] roles = checkRoles.roles();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String accountRole = jwt.getClaim("accountType");

        for (String role : roles) {
            if(role.equals(accountRole)) {
                return;
            }
        }
        throw new UnauthorizedException("Not authorized for this resource. Required role(s): " + Arrays.toString(roles) + " , Provided role: " + accountRole);
    }
}
