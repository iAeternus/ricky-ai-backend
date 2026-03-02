package com.ricky.ai.provider;

import com.ricky.ai.model.AiChunk;
import com.ricky.ai.model.ChatRequest;
import com.ricky.ai.model.ChatResponse;
import com.ricky.ai.model.ModelCapabilities;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AiProvider {
    Flux<AiChunk> streamChat(ChatRequest request);

    Mono<ChatResponse> chat(ChatRequest request);

    ModelCapabilities capabilities();
}
