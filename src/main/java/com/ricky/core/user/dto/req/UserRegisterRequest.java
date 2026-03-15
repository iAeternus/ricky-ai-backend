package com.ricky.core.user.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRegisterRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String displayName,
        String deviceId
) {
}
