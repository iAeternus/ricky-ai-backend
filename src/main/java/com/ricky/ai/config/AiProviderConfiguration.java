package com.ricky.ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.ai.gateway.AiProviderRegistry;
import com.ricky.ai.protocol.OllamaProtocolAdapter;
import com.ricky.ai.protocol.OpenAiProtocolAdapter;
import com.ricky.ai.provider.DeepSeekProvider;
import com.ricky.ai.provider.OllamaProvider;
import com.ricky.ai.provider.OpenAiProvider;
import com.ricky.model.domain.model.ProviderType;
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
