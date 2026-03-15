package com.ricky.core.user.infra;

import com.ricky.core.user.domain.User;
import com.ricky.core.user.domain.UserRole;
import com.ricky.core.user.domain.UserStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
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

    public static UserEntity from(String email, String passwordHash, String displayName) {
        return UserEntity.builder()
                .email(email)
                .passwordHash(passwordHash)
                .displayName(displayName)
                .role(UserRole.USER.name())
                .status(UserStatus.ACTIVE.name())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    public User into() {
        return new User(
                getId(),
                getEmail(),
                getPasswordHash(),
                getDisplayName(),
                UserRole.valueOf(getRole()),
                UserStatus.valueOf(getStatus()),
                getLastLoginAt(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

}
