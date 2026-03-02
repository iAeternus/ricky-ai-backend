package com.ricky.chat.dto;

import com.ricky.chat.domain.model.MessageRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageDto(
        @NotNull MessageRole role,
        @NotBlank String content
) {
}
