package com.ricky.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
