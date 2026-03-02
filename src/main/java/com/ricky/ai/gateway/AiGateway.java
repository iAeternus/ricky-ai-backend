package com.ricky.ai.gateway;

import com.ricky.ai.config.AiModelConfig;
import com.ricky.ai.config.AiModelConfigResolver;
import com.ricky.ai.model.AiChunk;
import com.ricky.ai.model.ChatRequest;
import com.ricky.ai.model.ChatResponse;
import com.ricky.ai.provider.AiProvider;
import com.ricky.common.exception.BizException;
import com.ricky.common.exception.ErrorCode;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class AiGateway {
    private final AiModelConfigResolver modelConfigResolver;
    private final AiProviderRegistry providerRegistry;

    public AiGateway(AiModelConfigResolver modelConfigResolver, AiProviderRegistry providerRegistry) {
        this.modelConfigResolver = modelConfigResolver;
        this.providerRegistry = providerRegistry;
    }

    public Flux<AiChunk> streamChat(ChatRequest request) {
        return resolveConfig(request)
                .flatMapMany(modelConfig -> resolveProvider(modelConfig)
                        .flatMapMany(provider -> provider.streamChat(withConfig(request, modelConfig))));
    }

    public Mono<ChatResponse> chat(ChatRequest request) {
        return resolveConfig(request)
                .flatMap(modelConfig -> resolveProvider(modelConfig)
                        .flatMap(provider -> provider.chat(withConfig(request, modelConfig))));
    }

    private Mono<AiModelConfig> resolveConfig(ChatRequest request) {
        return modelConfigResolver.resolveById(request.modelId())
                .filter(AiModelConfig::isEnabled)
                .switchIfEmpty(Mono.error(new BizException(ErrorCode.MODEL_DISABLED)));
    }

    private Mono<AiProvider> resolveProvider(AiModelConfig modelConfig) {
        return Mono.fromSupplier(() -> providerRegistry.get(modelConfig.getProviderType()));
    }

    private ChatRequest withConfig(ChatRequest request, AiModelConfig modelConfig) {
        return new ChatRequest(
                request.userId(),
                request.conversationId(),
                request.modelId(),
                request.messages(),
                request.temperature(),
                request.maxTokens(),
                request.extraParams(),
                modelConfig
        );
    }
}
