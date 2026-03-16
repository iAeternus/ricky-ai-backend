package com.ricky.core.chat.domain;

import org.springframework.stereotype.Component;

@Component
public class MessageFactory {

    public Message create(Long conversationId, Long userId, MessageRole role, String content) {
        return new Message(null, conversationId, userId, role, content, null, MessageStatus.ACTIVE);
    }

}
