package com.ricky.model.dto;

import com.ricky.model.domain.model.ProtocolType;
import com.ricky.model.domain.model.ProviderType;

import java.util.Map;

public record AiModelResponse(
        Long id,
        String name,
        ProviderType providerType,
        ProtocolType protocolType,
        String baseUrl,
        String modelName,
        boolean enabled,
        int priority,
        Map<String, Object> extraConfig
) {
}
