package com.ricky.core.chat.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import static com.ricky.common.utils.ValidationUtils.requireNonNull;

@Getter
@AllArgsConstructor
public class Message {
    private Long id;
    @NonNull
    private Long conversationId;
    @NonNull
    private Long userId;
    @NonNull
    private MessageRole role;
    @NonNull
    private String content;
    private Integer tokenCount;
    @NonNull
    private MessageStatus status;

    public void changeStatus(MessageStatus newStatus) {
        this.status = requireNonNull(newStatus, "newStatus");
    }
}
