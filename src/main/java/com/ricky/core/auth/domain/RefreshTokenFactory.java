package com.ricky.core.auth.domain;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RefreshTokenFactory {

    public RefreshToken create(Long userId, String token, Instant expiresAt) {
        return new RefreshToken(null, userId, token, expiresAt, false);
    }
}
