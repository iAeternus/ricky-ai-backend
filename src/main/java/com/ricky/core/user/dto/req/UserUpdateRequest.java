package com.ricky.core.user.dto.req;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateRequest(
        @NotBlank String displayName
) {
}
