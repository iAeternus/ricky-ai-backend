package com.ricky.apitest.model;

import com.ricky.apitest.BaseApiTest;
import com.ricky.apitest.ModelApi;
import com.ricky.apitest.SetupApi;
import com.ricky.core.auth.dto.resp.TokenResponse;
import com.ricky.core.model.domain.ProtocolType;
import com.ricky.core.model.domain.ProviderType;
import com.ricky.core.model.dto.req.AiModelCreateRequest;
import com.ricky.core.model.dto.resp.AiModelResponse;
import com.ricky.core.model.dto.req.AiModelUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ModelControllerTest extends BaseApiTest {

    @Autowired
    private SetupApi setupApi;

    @Test
    void createGetUpdateList() {
        TokenResponse token = setupApi.register();
        String accessToken = token.accessToken();

        AiModelCreateRequest createRequest = new AiModelCreateRequest(
                "test-model",
                ProviderType.OPENAI,
                ProtocolType.OPENAI_PROTOCOL,
                "https://api.example.com",
                "secret",
                "gpt-4o-mini",
                100,
                Map.of("stream", true)
        );

        AiModelResponse created = ModelApi.create(accessToken, createRequest);
        assertNotNull(created);
        assertNotNull(created.id());

        AiModelResponse fetched = ModelApi.get(accessToken, created.id());
        assertEquals(created.id(), fetched.id());

        AiModelUpdateRequest updateRequest = new AiModelUpdateRequest(
                "test-model-updated",
                ProviderType.OPENAI,
                ProtocolType.OPENAI_PROTOCOL,
                "https://api.example.com",
                "secret",
                "gpt-4o-mini",
                true,
                50,
                Map.of("stream", true)
        );

        AiModelResponse updated = ModelApi.update(accessToken, created.id(), updateRequest);
        assertEquals("test-model-updated", updated.name());

        List<AiModelResponse> models = ModelApi.list(accessToken);
        assertTrue(models.stream().anyMatch(model -> model.id().equals(created.id())));
    }
}
