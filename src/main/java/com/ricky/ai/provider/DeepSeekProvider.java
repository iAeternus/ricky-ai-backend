package com.ricky.ai.provider;

import com.ricky.ai.config.AiModelConfig;
import com.ricky.ai.model.ChatRequest;
import com.ricky.ai.model.ModelCapabilities;
import com.ricky.ai.protocol.ProtocolAdapter;
import com.ricky.ai.protocol.ProtocolContext;

import java.time.Duration;

public class DeepSeekProvider extends AbstractAiProvider {
    public DeepSeekProvider(ProtocolAdapter protocolAdapter) {
        super(protocolAdapter);
    }

    @Override
    protected ProtocolContext buildContext(ChatRequest request) {
        AiModelConfig modelConfig = requireModelConfig(request);
        return new ProtocolContext(
                modelConfig.getBaseUrl(),
                modelConfig.getApiKey(),
                modelConfig.getModelName(),
                Duration.ofSeconds(60),
                modelConfig.getExtraConfig()
        );
    }

    @Override
    protected ModelCapabilities modelCapabilities() {
        return new ModelCapabilities(true, false, false);
    }
}
