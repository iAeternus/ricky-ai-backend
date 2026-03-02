package com.ricky.ai.protocol;

import com.ricky.ai.model.AiChunk;
import com.ricky.ai.model.ChatRequest;
import com.ricky.ai.model.ChatResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProtocolAdapter {
    Flux<AiChunk> streamChat(ChatRequest request, ProtocolContext context);

    Mono<ChatResponse> chat(ChatRequest request, ProtocolContext context);
}
