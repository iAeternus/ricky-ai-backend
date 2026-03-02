package com.ricky.chat;

import com.ricky.ai.gateway.AiGateway;
import com.ricky.ai.model.AiChunk;
import com.ricky.ai.model.ChatRequest;
import com.ricky.chat.application.ChatAppService;
import com.ricky.chat.application.ChatStreamManager;
import com.ricky.chat.domain.model.MessageRole;
import com.ricky.chat.dto.ChatMessageDto;
import com.ricky.chat.dto.ChatStreamRequest;
import com.ricky.chat.dto.ChatStreamResponse;
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

        ChatAppService service = new ChatAppService(gateway, new ChatStreamManager());

        ChatStreamRequest request = new ChatStreamRequest(
                "req-1",
                1L,
                2L,
                List.of(new ChatMessageDto(MessageRole.USER, "hi")),
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
