package com.ricky.core.ai.protocol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.core.ai.model.AiChunk;
import com.ricky.core.ai.model.ChatMessage;
import com.ricky.core.ai.model.ChatRequest;
import com.ricky.core.ai.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OllamaProtocolAdapter implements ProtocolAdapter {
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;

    public OllamaProtocolAdapter(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<AiChunk> streamChat(ChatRequest request, ProtocolContext context) {
        WebClient client = webClientBuilder.baseUrl(context.baseUrl()).build();
        Map<String, Object> payload = buildPayload(request, context);
        payload.put("stream", true);

        return client.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_NDJSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToFlux(String.class)
                .flatMap(line -> parseStreamLine(line, context.modelName()));
    }

    @Override
    public Mono<ChatResponse> chat(ChatRequest request, ProtocolContext context) {
        WebClient client = webClientBuilder.baseUrl(context.baseUrl()).build();
        Map<String, Object> payload = buildPayload(request, context);
        payload.put("stream", false);

        return client.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> parseChatResponse(body, context.modelName()));
    }

    private Map<String, Object> buildPayload(ChatRequest request, ProtocolContext context) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", context.modelName());
        payload.put("messages", toOllamaMessages(request.messages()));
        if (request.extraParams() != null) {
            payload.putAll(request.extraParams());
        }
        return payload;
    }

    private List<Map<String, String>> toOllamaMessages(List<ChatMessage> messages) {
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

    private Flux<AiChunk> parseStreamLine(String line, String modelName) {
        try {
            JsonNode root = objectMapper.readTree(line);
            JsonNode message = root.path("message");
            String content = message.path("content").asText("");
            boolean done = root.path("done").asBoolean(false);
            if (content.isBlank() && !done) {
                return Flux.empty();
            }
            return Flux.just(new AiChunk(content, done, modelName, Map.of()));
        } catch (Exception ex) {
            return Flux.error(ex);
        }
    }

    private ChatResponse parseChatResponse(String body, String modelName) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode message = root.path("message");
            String content = message.path("content").asText("");
            String responseModel = root.path("model").asText(modelName);
            return new ChatResponse(content, responseModel, Map.of());
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to parse Ollama response", ex);
        }
    }
}
