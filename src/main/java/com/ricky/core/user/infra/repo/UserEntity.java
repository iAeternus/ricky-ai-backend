package com.ricky.core.user.infra.repo;

import com.ricky.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Table("users")
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {

    private String email;
    @Column("password_hash")
    private String passwordHash;
    @Column("display_name")
    private String displayName;
    private String role;
    private String status;
    @Column("last_login_at")
    private Instant lastLoginAt;

}
