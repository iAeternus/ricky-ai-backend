package com.ricky.core.auth.infra;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("refresh_tokens")
public class RefreshTokenEntity {

    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    private String token;
    @Column("expires_at")
    private Instant expiresAt;
    private boolean revoked;
    @Column("created_at")
    private Instant createdAt;

}
