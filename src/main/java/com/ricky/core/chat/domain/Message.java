package com.ricky.core.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Message {
    private final Long id;
    @NonNull
    private final Long conversationId;
    @NonNull
    private final Long userId;
    @NonNull
    private final MessageRole role;
    @NonNull
    private final String content;
    private final Integer tokenCount;
    @NonNull
    private final MessageStatus status;
    @NonNull
    private final Instant createdAt;

    public Message withStatus(MessageStatus newStatus) {
        return new Message(id, conversationId, userId, role, content, tokenCount, newStatus, createdAt);
    }
}
