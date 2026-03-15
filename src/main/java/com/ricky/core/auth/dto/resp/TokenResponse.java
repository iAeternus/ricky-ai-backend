package com.ricky.core.auth.dto.resp;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
