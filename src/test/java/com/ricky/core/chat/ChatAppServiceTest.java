package com.ricky.core.chat;

import com.ricky.core.ai.gateway.AiGateway;
import com.ricky.core.ai.model.AiChunk;
import com.ricky.core.ai.model.ChatRequest;
import com.ricky.core.chat.service.ChatStreamManager;
import com.ricky.core.chat.service.impl.ChatAppServiceImpl;
import com.ricky.core.chat.domain.MessageRole;
import com.ricky.core.chat.dto.req.ChatMessageRequest;
import com.ricky.core.chat.dto.req.ChatStreamRequest;
import com.ricky.core.chat.dto.resp.ChatStreamResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ChatAppServiceTest {

    @Test
    void streamChatEmitsDeltaAndDone() {
        AtomicReference<ChatRequest> captured = new AtomicReference<>();
        AiGateway gateway = new AiGateway(null, null) {
            @Override
            public Flux<AiChunk> streamChat(ChatRequest request) {
                captured.set(request);
                return Flux.just(
                        new AiChunk("hello", false, "gpt", Map.of()),
                        new AiChunk("world", true, "gpt", Map.of("finishReason", "stop"))
                );
            }
        };

        ChatAppServiceImpl service = new ChatAppServiceImpl(gateway, new ChatStreamManager());

        ChatStreamRequest request = new ChatStreamRequest(
                "req-1",
                1L,
                2L,
                List.of(new ChatMessageRequest(MessageRole.USER, "hi")),
                0.7,
                100,
                Map.of()
        );

        Flux<ServerSentEvent<ChatStreamResponse>> stream = service.streamChat(request)
                .filter(event -> event.data() != null && !"heartbeat".equals(event.data().type()));

        StepVerifier.create(stream)
                .expectNextMatches(event -> "delta".equals(event.data().type()) && "hello".equals(event.data().content()))
                .expectNextMatches(event -> "delta".equals(event.data().type()) && "world".equals(event.data().content()))
                .expectNextMatches(event -> "done".equals(event.data().type()))
                .verifyComplete();

        ChatRequest built = captured.get();
        assertNotNull(built);
        assertEquals(1L, built.modelId());
        assertEquals(1, built.messages().size());
    }
}
