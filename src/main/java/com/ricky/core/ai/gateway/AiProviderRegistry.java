package com.ricky.core.ai.gateway;

import com.ricky.core.ai.provider.AiProvider;
import com.ricky.core.model.domain.model.ProviderType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class AiProviderRegistry {
    private final Map<ProviderType, AiProvider> providers;

    public AiProviderRegistry(List<AiProviderEntry> entries) {
        Map<ProviderType, AiProvider> map = new EnumMap<>(ProviderType.class);
        for (AiProviderEntry entry : entries) {
            if (map.put(entry.providerType(), entry.provider()) != null) {
                throw new IllegalStateException("Duplicate provider registration: " + entry.providerType());
            }
        }
        this.providers = Map.copyOf(map);
    }

    public AiProvider get(ProviderType providerType) {
        AiProvider provider = providers.get(providerType);
        if (provider == null) {
            throw new IllegalArgumentException("Provider not found: " + providerType);
        }
        return provider;
    }

    public record AiProviderEntry(ProviderType providerType, AiProvider provider) {
        public AiProviderEntry {
            Objects.requireNonNull(providerType, "providerType");
            Objects.requireNonNull(provider, "provider");
        }
    }
}
