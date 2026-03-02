package com.ricky.ai.model;

import com.ricky.chat.domain.model.MessageRole;

public record ChatMessage(
        MessageRole role,
        String content
) {
}
