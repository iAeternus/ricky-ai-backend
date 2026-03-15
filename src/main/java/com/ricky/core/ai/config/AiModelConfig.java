package com.ricky.core.ai.config;

import com.ricky.core.model.domain.model.ProtocolType;
import com.ricky.core.model.domain.model.ProviderType;
import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class AiModelConfig {
    Long id;
    String name;
    ProviderType providerType;
    ProtocolType protocolType;
    String baseUrl;
    String apiKey;
    String modelName;
    boolean enabled;
    int priority;
    Map<String, Object> extraConfig;
}
