package com.ricky.core.ai.model;

import com.ricky.core.chat.domain.model.MessageRole;

public record ChatMessage(
        MessageRole role,
        String content
) {
}
