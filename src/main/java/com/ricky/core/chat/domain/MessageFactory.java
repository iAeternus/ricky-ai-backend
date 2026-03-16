package com.ricky.core.chat.domain;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MessageFactory {

    public Message create(Long conversationId, Long userId, MessageRole role, String content) {
        Instant now = Instant.now();
        return new Message(null, conversationId, userId, role, content, null, MessageStatus.ACTIVE, now);
    }

}
