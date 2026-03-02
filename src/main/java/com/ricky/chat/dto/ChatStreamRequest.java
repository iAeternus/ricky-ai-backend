package com.ricky.chat.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public record ChatStreamRequest(
        String requestId,
        @NotNull Long modelId,
        Long conversationId,
        @NotNull List<ChatMessageDto> messages,
        Double temperature,
        Integer maxTokens,
        Map<String, Object> extraParams
) {
}
