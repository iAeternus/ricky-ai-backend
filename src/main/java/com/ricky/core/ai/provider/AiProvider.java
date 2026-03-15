package com.ricky.core.ai.provider;

import com.ricky.core.ai.model.AiChunk;
import com.ricky.core.ai.model.ChatRequest;
import com.ricky.core.ai.model.ChatResponse;
import com.ricky.core.ai.model.ModelCapabilities;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AiProvider {
    Flux<AiChunk> streamChat(ChatRequest request);

    Mono<ChatResponse> chat(ChatRequest request);

    ModelCapabilities capabilities();
}
