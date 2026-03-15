package com.ricky.core.ai.protocol;

import com.ricky.core.ai.model.AiChunk;
import com.ricky.core.ai.model.ChatRequest;
import com.ricky.core.ai.model.ChatResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProtocolAdapter {
    Flux<AiChunk> streamChat(ChatRequest request, ProtocolContext context);

    Mono<ChatResponse> chat(ChatRequest request, ProtocolContext context);
}
