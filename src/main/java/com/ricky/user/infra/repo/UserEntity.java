package com.ricky.user.infra.repo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("users")
public class UserEntity {

    @Id
    private Long id;
    private String email;
    @Column("password_hash")
    private String passwordHash;
    @Column("display_name")
    private String displayName;
    private String role;
    private String status;
    @Column("last_login_at")
    private Instant lastLoginAt;
    @Column("created_at")
    private Instant createdAt;
    @Column("updated_at")
    private Instant updatedAt;

}
