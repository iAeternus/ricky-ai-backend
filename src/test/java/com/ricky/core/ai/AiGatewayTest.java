package com.ricky.core.ai;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.ai.config.AiModelConfigResolver;
import com.ricky.core.ai.gateway.AiGateway;
import com.ricky.core.ai.gateway.AiProviderRegistry;
import com.ricky.core.ai.model.*;
import com.ricky.core.ai.provider.AiProvider;
import com.ricky.core.chat.domain.MessageRole;
import com.ricky.core.model.domain.ProtocolType;
import com.ricky.core.model.domain.ProviderType;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AiGatewayTest {

    @Test
    void streamChatInjectsModelConfig() {
        AiModelConfig config = AiModelConfig.builder()
                .id(1L)
                .name("openai")
                .providerType(ProviderType.OPENAI)
                .protocolType(ProtocolType.OPENAI_PROTOCOL)
                .baseUrl("https://api.example.com")
                .apiKey("secret")
                .modelName("gpt-test")
                .enabled(true)
                .priority(1)
                .extraConfig(Map.of())
                .build();

        AiModelConfigResolver resolver = modelId -> Mono.just(config);

        AtomicReference<ChatRequest> captured = new AtomicReference<>();
        AiGateway gateway = makeAiGateway(captured, resolver);

        ChatRequest request = new ChatRequest(
                10L,
                20L,
                1L,
                List.of(new ChatMessage(MessageRole.USER, "hello")),
                0.7,
                100,
                Map.of(),
                null
        );

        StepVerifier.create(gateway.streamChat(request))
                .expectNextMatches(chunk -> "hi".equals(chunk.content()))
                .verifyComplete();

        ChatRequest injected = captured.get();
        assertNotNull(injected);
        assertNotNull(injected.modelConfig());
        assertEquals("gpt-test", injected.modelConfig().getModelName());
    }

    private static AiGateway makeAiGateway(AtomicReference<ChatRequest> captured, AiModelConfigResolver resolver) {
        AiProvider provider = new AiProvider() {
            @Override
            public Flux<AiChunk> streamChat(ChatRequest request) {
                captured.set(request);
                return Flux.just(new AiChunk("hi", true, "gpt-test", Map.of()));
            }

            @Override
            public Mono<ChatResponse> chat(ChatRequest request) {
                captured.set(request);
                return Mono.just(new ChatResponse("ok", "gpt-test", Map.of()));
            }

            @Override
            public ModelCapabilities capabilities() {
                return new ModelCapabilities(true, false, false);
            }
        };

        AiProviderRegistry registry = new AiProviderRegistry(
                List.of(new AiProviderRegistry.AiProviderEntry(ProviderType.OPENAI, provider))
        );

        return new AiGateway(resolver, registry);
    }
}
