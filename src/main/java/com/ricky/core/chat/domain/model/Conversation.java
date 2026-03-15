package com.ricky.core.chat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class Conversation {
    private final Long id;
    @NonNull
    private final Long userId;
    private final Long modelId;
    @NonNull
    private final String title;
    private final boolean pinned;
    @NonNull
    private final ConversationStatus status;
    @NonNull
    private final Instant createdAt;
    @NonNull
    private final Instant updatedAt;

    public Conversation rename(String newTitle) {
        return new Conversation(id, userId, modelId, newTitle, pinned, status, createdAt, Instant.now());
    }

    public Conversation pin(boolean newPinned) {
        return new Conversation(id, userId, modelId, title, newPinned, status, createdAt, Instant.now());
    }

    public Conversation withStatus(ConversationStatus newStatus) {
        return new Conversation(id, userId, modelId, title, pinned, newStatus, createdAt, Instant.now());
    }
}
