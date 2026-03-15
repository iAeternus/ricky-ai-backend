package com.ricky.core.model.domain;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AiModelFactory {

    public AiModel create(
            String name,
            ProviderType providerType,
            ProtocolType protocolType,
            String baseUrl,
            String apiKey,
            String modelName,
            Integer priority,
            Map<String, Object> extraConfig
    ) {
        return new AiModel(
                null,
                name,
                providerType,
                protocolType,
                baseUrl,
                apiKey,
                modelName,
                ModelStatus.ENABLED,
                priority == null ? 100 : priority,
                extraConfig
        );
    }
}
