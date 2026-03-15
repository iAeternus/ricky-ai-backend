package com.ricky.core.model.dto.req;

import com.ricky.core.model.domain.ProtocolType;
import com.ricky.core.model.domain.ProviderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record AiModelUpdateRequest(
        @NotBlank String name,
        @NotNull ProviderType providerType,
        @NotNull ProtocolType protocolType,
        @NotBlank String baseUrl,
        @NotBlank String apiKey,
        @NotBlank String modelName,
        Boolean enabled,
        Integer priority,
        Map<String, Object> extraConfig
) {
}
