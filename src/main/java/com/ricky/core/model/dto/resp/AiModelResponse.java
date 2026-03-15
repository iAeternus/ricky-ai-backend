package com.ricky.core.model.dto.resp;

import com.ricky.core.model.domain.ProtocolType;
import com.ricky.core.model.domain.ProviderType;

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
