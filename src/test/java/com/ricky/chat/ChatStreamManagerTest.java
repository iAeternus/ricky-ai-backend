package com.ricky.chat;

import com.ricky.chat.application.ChatStreamManager;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class ChatStreamManagerTest {

    @Test
    void cancelSignalCompletes() {
        ChatStreamManager manager = new ChatStreamManager();
        StepVerifier.create(manager.register("req-1"))
                .then(() -> manager.cancel("req-1"))
                .verifyComplete();
    }
}
