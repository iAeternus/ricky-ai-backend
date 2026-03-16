package com.ricky.core.chat.domain;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ConversationFactory {

    public Conversation create(Long userId, Long modelId, String title) {
        Instant now = Instant.now();
        return new Conversation(null, userId, modelId, title, false, ConversationStatus.ACTIVE, now, now);
    }

}
