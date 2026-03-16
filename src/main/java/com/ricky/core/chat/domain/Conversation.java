package com.ricky.core.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import static com.ricky.common.utils.ValidationUtils.requireNonNull;

@Getter
@AllArgsConstructor
public class Conversation {
    private Long id;
    @NonNull
    private Long userId;
    private Long modelId;
    @NonNull
    private String title;
    private boolean pinned;
    @NonNull
    private ConversationStatus status;

    public void rename(String newTitle) {
        this.title = requireNonNull(newTitle, "newTitle");
    }

    public void pin(boolean newPinned) {
        this.pinned = newPinned;
    }

    public void changeStatus(ConversationStatus newStatus) {
        this.status = requireNonNull(newStatus, "newStatus");
    }
}
