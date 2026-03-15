package com.ricky.core.ai.provider;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.ai.model.AiChunk;
import com.ricky.core.ai.model.ChatRequest;
import com.ricky.core.ai.model.ChatResponse;
import com.ricky.core.ai.model.ModelCapabilities;
import com.ricky.core.ai.protocol.ProtocolAdapter;
import com.ricky.core.ai.protocol.ProtocolContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class AbstractAiProvider implements AiProvider {

    private final ProtocolAdapter protocolAdapter;

    protected AbstractAiProvider(ProtocolAdapter protocolAdapter) {
        this.protocolAdapter = protocolAdapter;
    }

    protected abstract ProtocolContext buildContext(ChatRequest request);

    protected abstract ModelCapabilities modelCapabilities();

    protected AiModelConfig requireModelConfig(ChatRequest request) {
        AiModelConfig modelConfig = request.modelConfig();
        if (modelConfig == null) {
            throw new IllegalArgumentException("Model config missing in request");
        }
        return modelConfig;
    }

    @Override
    public Flux<AiChunk> streamChat(ChatRequest request) {
        return protocolAdapter.streamChat(request, buildContext(request));
    }

    @Override
    public Mono<ChatResponse> chat(ChatRequest request) {
        return protocolAdapter.chat(request, buildContext(request));
    }

    @Override
    public ModelCapabilities capabilities() {
        return modelCapabilities();
    }
}
