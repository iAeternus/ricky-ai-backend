package com.ricky.user.dto;

public record UserProfileResponse(
        Long id,
        String email,
        String displayName,
        String role,
        String status
) {
}
