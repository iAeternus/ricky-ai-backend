package com.ricky.core.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank String refreshToken,
        String deviceId
) {
}
