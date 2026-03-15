package com.ricky.core.ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.core.ai.gateway.AiProviderRegistry;
import com.ricky.core.ai.protocol.OllamaProtocolAdapter;
import com.ricky.core.ai.protocol.OpenAiProtocolAdapter;
import com.ricky.core.ai.provider.DeepSeekProvider;
import com.ricky.core.ai.provider.OllamaProvider;
import com.ricky.core.ai.provider.OpenAiProvider;
import com.ricky.core.model.domain.ProviderType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiProviderConfiguration {
    @Bean
    public OpenAiProtocolAdapter openAiProtocolAdapter(WebClient.Builder builder, ObjectMapper objectMapper) {
        return new OpenAiProtocolAdapter(builder, objectMapper);
    }

    @Bean
    public OllamaProtocolAdapter ollamaProtocolAdapter(WebClient.Builder builder, ObjectMapper objectMapper) {
        return new OllamaProtocolAdapter(builder, objectMapper);
    }

    @Bean
    public OpenAiProvider openAiProvider(OpenAiProtocolAdapter adapter) {
        return new OpenAiProvider(adapter);
    }

    @Bean
    public DeepSeekProvider deepSeekProvider(OpenAiProtocolAdapter adapter) {
        return new DeepSeekProvider(adapter);
    }

    @Bean
    public OllamaProvider ollamaProvider(OllamaProtocolAdapter adapter) {
        return new OllamaProvider(adapter);
    }

    @Bean
    public AiProviderRegistry.AiProviderEntry openAiProviderEntry(OpenAiProvider provider) {
        return new AiProviderRegistry.AiProviderEntry(ProviderType.OPENAI, provider);
    }

    @Bean
    public AiProviderRegistry.AiProviderEntry deepSeekProviderEntry(DeepSeekProvider provider) {
        return new AiProviderRegistry.AiProviderEntry(ProviderType.DEEPSEEK, provider);
    }

    @Bean
    public AiProviderRegistry.AiProviderEntry ollamaProviderEntry(OllamaProvider provider) {
        return new AiProviderRegistry.AiProviderEntry(ProviderType.OLLAMA, provider);
    }
}
