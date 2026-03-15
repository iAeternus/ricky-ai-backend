package com.ricky.core.ai.model;

import com.ricky.core.ai.config.AiModelConfig;

import java.util.List;
import java.util.Map;

public record ChatRequest(
        Long userId,
        Long conversationId,
        Long modelId,
        List<ChatMessage> messages,
        Double temperature,
        Integer maxTokens,
        Map<String, Object> extraParams,
        AiModelConfig modelConfig
) {
}
