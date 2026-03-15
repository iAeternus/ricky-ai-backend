package com.ricky.core.ai.protocol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.core.ai.model.AiChunk;
import com.ricky.core.ai.model.ChatMessage;
import com.ricky.core.ai.model.ChatRequest;
import com.ricky.core.ai.model.ChatResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenAiProtocolAdapter implements ProtocolAdapter {
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    public OpenAiProtocolAdapter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<AiChunk> streamChat(ChatRequest request, ProtocolContext context) {
        WebClient client = buildClient(context);
        Map<String, Object> payload = buildPayload(request, context);
        payload.put("stream", true);

        return client.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(payload)
                .retrieve()
                .bodyToFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
                })
                .mapNotNull(ServerSentEvent::data)
                .flatMap(data -> parseStreamEvent(data, context.modelName()));
    }

    @Override
    public Mono<ChatResponse> chat(ChatRequest request, ProtocolContext context) {
        WebClient client = buildClient(context);
        Map<String, Object> payload = buildPayload(request, context);

        return client.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> parseChatResponse(body, context.modelName()));
    }

    private WebClient buildClient(ProtocolContext context) {
        WebClient.Builder builder = webClientBuilder.baseUrl(context.baseUrl());
        if (context.apiKey() != null && !context.apiKey().isBlank()) {
            builder.defaultHeader("Authorization", "Bearer " + context.apiKey());
        }
        return builder.build();
    }

    private Map<String, Object> buildPayload(ChatRequest request, ProtocolContext context) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", context.modelName());
        payload.put("messages", toOpenAiMessages(request.messages()));
        if (request.temperature() != null) {
            payload.put("temperature", request.temperature());
        }
        if (request.maxTokens() != null) {
            payload.put("max_tokens", request.maxTokens());
        }
        if (request.extraParams() != null) {
            payload.putAll(request.extraParams());
        }
        return payload;
    }

    private List<Map<String, String>> toOpenAiMessages(List<ChatMessage> messages) {
        if (messages == null) {
            return List.of();
        }
        return messages.stream()
                .map(message -> Map.of(
                        "role", message.role().name().toLowerCase(),
                        "content", message.content()
                ))
                .toList();
    }

    private Flux<AiChunk> parseStreamEvent(String data, String modelName) {
        if ("[DONE]".equals(data)) {
            return Flux.just(new AiChunk("", true, modelName, Map.of("finishReason", "stop")));
        }
        try {
            JsonNode root = objectMapper.readTree(data);
            JsonNode choice = root.path("choices").path(0);
            String content = choice.path("delta").path("content").asText(null);
            String finishReason = choice.path("finish_reason").isMissingNode() ? null : choice.path("finish_reason").asText(null);
            boolean finished = finishReason != null;
            Map<String, Object> metadata = new HashMap<>();
            if (finishReason != null && !finishReason.isBlank()) {
                metadata.put("finishReason", finishReason);
            }
            if (content == null || content.isBlank()) {
                return finished
                        ? Flux.just(new AiChunk("", true, modelName, Map.copyOf(metadata)))
                        : Flux.empty();
            }
            return Flux.just(new AiChunk(content, finished, modelName, Map.copyOf(metadata)));
        } catch (Exception ex) {
            return Flux.error(ex);
        }
    }

    private ChatResponse parseChatResponse(String body, String modelName) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode choice = root.path("choices").path(0);
            String content = choice.path("message").path("content").asText("");
            String responseModel = root.path("model").asText(modelName);
            return new ChatResponse(content, responseModel, Map.of());
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse OpenAI response", ex);
        }
    }
}
