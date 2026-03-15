package com.ricky.core.common.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.postgresql.codec.Json;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static com.ricky.common.utils.ValidationUtils.isBlank;

@Component
@RequiredArgsConstructor
public class JsonMapMapper {

    private final ObjectMapper objectMapper;

    @Named("jsonToMap")
    public Map<String, Object> jsonToMap(Json json) {
        if (json == null || isBlank(json.asString())) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json.asString(), new TypeReference<>() {
            });
        } catch (Exception ex) {
            return Collections.emptyMap();
        }
    }

    @Named("mapToJson")
    public Json mapToJson(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        try {
            return Json.of(objectMapper.writeValueAsString(map));
        } catch (Exception ex) {
            return null;
        }
    }
}
