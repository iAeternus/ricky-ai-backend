package com.ricky.common.security;

public record JwtPrincipal(
        Long userId,
        String email,
        String role
) {
}
