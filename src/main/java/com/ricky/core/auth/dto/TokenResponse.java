package com.ricky.core.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
