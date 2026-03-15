package com.ricky.core.ai.provider;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.ai.model.ChatRequest;
import com.ricky.core.ai.model.ModelCapabilities;
import com.ricky.core.ai.protocol.ProtocolAdapter;
import com.ricky.core.ai.protocol.ProtocolContext;

import java.time.Duration;

public class OllamaProvider extends AbstractAiProvider {
    public OllamaProvider(ProtocolAdapter protocolAdapter) {
        super(protocolAdapter);
    }

    @Override
    protected ProtocolContext buildContext(ChatRequest request) {
        AiModelConfig modelConfig = requireModelConfig(request);
        return new ProtocolContext(
                modelConfig.getBaseUrl(),
                modelConfig.getApiKey(),
                modelConfig.getModelName(),
                Duration.ofSeconds(120),
                modelConfig.getExtraConfig()
        );
    }

    @Override
    protected ModelCapabilities modelCapabilities() {
        return new ModelCapabilities(true, false, false);
    }
}
