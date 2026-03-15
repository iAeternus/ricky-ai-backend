package com.ricky.apitest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricky.RickyAiBackendApplication;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.config.RestAssuredConfig.config;
import static io.restassured.http.ContentType.JSON;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(classes = RickyAiBackendApplication.class, webEnvironment = RANDOM_PORT)
@Import(FlywayTestConfig.class)
public abstract class BaseApiTest {

    @LocalServerPort
    protected int port;

    private static ObjectMapper STATIC_OBJECT_MAPPER;

    @Autowired
    void setObjectMapper(ObjectMapper objectMapper) {
        STATIC_OBJECT_MAPPER = objectMapper;
    }

    public static RequestSpecification given() {
        return RestAssured.given()
                .config(config()
                        .objectMapperConfig(new ObjectMapperConfig()
                                .jackson2ObjectMapperFactory((type, s) -> STATIC_OBJECT_MAPPER))
                        .encoderConfig(new EncoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))
                        .logConfig(LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails()));
    }

    public static RequestSpecification givenBearer(String token) {
        if (token == null || token.isBlank()) {
            return given();
        }
        return given().header("Authorization", "Bearer " + token);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(JSON)
                .setAccept(JSON)
                .build();
    }
}
