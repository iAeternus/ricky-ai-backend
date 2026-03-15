package com.ricky.apitest.chat;

import com.ricky.common.security.JwtService;
import com.ricky.core.ai.gateway.AiGateway;
import com.ricky.core.ai.model.AiChunk;
import com.ricky.core.chat.dto.req.ChatMessageDto;
import com.ricky.core.chat.dto.req.ChatStreamRequest;
import com.ricky.core.chat.domain.MessageRole;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(com.ricky.apitest.FlywayTestConfig.class)
class ChatControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtService jwtService;

    @MockitoBean
    private AiGateway aiGateway;

    @Test
    void streamChatReturnsSse() {
        Mockito.when(aiGateway.streamChat(Mockito.any()))
                .thenReturn(Flux.just(new AiChunk("hi", true, "gpt", Map.of())));

        String token = jwtService.generateAccessToken(1L, "test@example.com", "USER");
        ChatStreamRequest request = new ChatStreamRequest(
                null,
                1L,
                null,
                List.of(new ChatMessageDto(MessageRole.USER, "hi")),
                null,
                null,
                Map.of()
        );

        webTestClient.post()
                .uri("/api/chat/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .header("Authorization", "Bearer " + token)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBody(String.class)
                .value(body -> org.junit.jupiter.api.Assertions.assertTrue(body.contains("delta")));
    }

    @Test
    void cancelReturnsBoolean() {
        String token = jwtService.generateAccessToken(1L, "test@example.com", "USER");
        webTestClient.post()
                .uri("/api/chat/cancel/{requestId}", "req-1")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isEqualTo(false);
    }
}
