package com.ricky.core.chat.dto.req;

import com.ricky.core.chat.domain.MessageRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest(
        @NotNull MessageRole role,
        @NotBlank String content
) {
}
