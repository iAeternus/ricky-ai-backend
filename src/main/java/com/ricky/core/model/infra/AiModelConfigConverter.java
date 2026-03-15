package com.ricky.core.model.infra;

import com.ricky.core.ai.config.AiModelConfig;
import com.ricky.core.model.domain.AiModel;
import com.ricky.core.model.domain.ModelStatus;
import org.springframework.stereotype.Component;

@Component
public class AiModelConfigConverter {
    public AiModelConfig toConfig(AiModel model) {
        return AiModelConfig.builder()
                .id(model.getId())
                .name(model.getName())
                .providerType(model.getProviderType())
                .protocolType(model.getProtocolType())
                .baseUrl(model.getBaseUrl())
                .apiKey(model.getApiKey())
                .modelName(model.getModelName())
                .enabled(model.getStatus() == ModelStatus.ENABLED)
                .priority(model.getPriority())
                .extraConfig(model.getExtraConfig())
                .build();
    }
}
