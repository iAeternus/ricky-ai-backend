package com.ricky.core.model.dto;

import com.ricky.core.model.domain.model.ProtocolType;
import com.ricky.core.model.domain.model.ProviderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record AiModelCreateRequest(
        @NotBlank String name,
        @NotNull ProviderType providerType,
        @NotNull ProtocolType protocolType,
        @NotBlank String baseUrl,
        @NotBlank String apiKey,
        @NotBlank String modelName,
        Integer priority,
        Map<String, Object> extraConfig
) {
}
