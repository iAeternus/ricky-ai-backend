package com.ricky.core.chat.domain;

import org.springframework.stereotype.Component;

@Component
public class ConversationFactory {

    public Conversation create(Long userId, Long modelId, String title) {
        return new Conversation(null, userId, modelId, title, false, ConversationStatus.ACTIVE);
    }

}
