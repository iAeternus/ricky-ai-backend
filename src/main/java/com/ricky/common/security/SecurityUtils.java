package com.ricky.common.security;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public class SecurityUtils {

    public static Mono<Long> currentUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> {
                    Object principal = auth.getPrincipal();
                    if (principal instanceof JwtPrincipal jwtPrincipal) {
                        return jwtPrincipal.userId();
                    }
                    if (principal instanceof UserDetails userDetails) {
                        return Long.parseLong(userDetails.getUsername());
                    }
                    return Long.parseLong(String.valueOf(principal));
                });
    }

}
