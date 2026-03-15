package com.ricky.core.user.dto.resp;

public record UserProfileResponse(
        Long id,
        String email,
        String displayName,
        String role,
        String status
) {
}
