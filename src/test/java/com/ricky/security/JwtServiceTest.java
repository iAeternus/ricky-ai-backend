package com.ricky.security;

import com.ricky.common.security.JwtProperties;
import com.ricky.common.security.JwtService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtServiceTest {

    @Test
    void generateAndParseAccessToken() {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("12345678901234567890123456789012");
        properties.setIssuer("test-issuer");

        JwtService service = new JwtService(properties);
        String token = service.generateAccessToken(99L, "test@example.com", "USER");

        var principal = service.parse(token);
        assertEquals(99L, principal.userId());
        assertEquals("test@example.com", principal.email());
        assertEquals("USER", principal.role());
    }
}
