package com.ricky.core.chat.dto.req;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.ricky.common.utils.ValidationUtils.isBlank;

public record ChatStreamRequest(
        String requestId,
        @NotNull Long modelId,
        Long conversationId,
        @NotNull List<ChatMessageRequest> messages,
        Double temperature,
        Integer maxTokens,
        Map<String, Object> extraParams
) {

    public String getRequestId() {
        return isBlank(requestId)  ? UUID.randomUUID().toString() : requestId;
    }

}
